package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.UserBelong;
import com.javarush.jira.bugtracking.UserBelongRepository;
import com.javarush.jira.bugtracking.sprint.Sprint;
import com.javarush.jira.bugtracking.sprint.SprintRepository;
import com.javarush.jira.bugtracking.task.exception.ValidationException;
import com.javarush.jira.bugtracking.task.mapper.TaskExtMapper;
import com.javarush.jira.bugtracking.task.mapper.TaskFullMapper;
import com.javarush.jira.bugtracking.task.to.TaskToExt;
import com.javarush.jira.bugtracking.task.to.TaskToFull;
import com.javarush.jira.common.error.DataConflictException;
import com.javarush.jira.common.error.NotFoundException;
import com.javarush.jira.common.util.Util;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.ref.RefType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.javarush.jira.bugtracking.ObjectType.TASK;
import static com.javarush.jira.bugtracking.task.TaskUtil.fillExtraFields;
import static com.javarush.jira.bugtracking.task.TaskUtil.makeActivity;
import static com.javarush.jira.ref.ReferenceService.getRefTo;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    static final String CANNOT_ASSIGN = "Cannot assign as %s to task with status=%s";
    static final String CANNOT_UN_ASSIGN = "Cannot unassign as %s from task with status=%s";

    static final String TASK_STATUS_IN_PROGRESS = "in_progress";
    static final String TASK_STATUS_READY_FOR_REVIEW = "ready_for_review";
    static final String TASK_STATUS_DONE = "done";

    private final Handlers.TaskExtHandler handler;
    private final Handlers.ActivityHandler activityHandler;
    private final TaskFullMapper fullMapper;
    private final SprintRepository sprintRepository;
    private final TaskExtMapper extMapper;
    private final UserBelongRepository userBelongRepository;
    private final ActivityRepository activityRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public void changeStatus(long taskId, String statusCode) {
        Assert.notNull(statusCode, "statusCode must not be null");
        Task task = handler.getRepository().getExisted(taskId);
        if (!statusCode.equals(task.getStatusCode())) {
            task.checkAndSetStatusCode(statusCode);
            Activity statusChangedActivity = new Activity(null, taskId, AuthUser.authId());
            statusChangedActivity.setStatusCode(statusCode);
            activityHandler.create(statusChangedActivity);
            String userType = getRefTo(RefType.TASK_STATUS, statusCode).getAux(1);
            if (userType != null) {
                handler.createUserBelong(taskId, TASK, AuthUser.authId(), userType);
            }
        }
    }

    @Transactional
    public void changeSprint(long taskId, Long sprintId) {
        Task task = handler.getRepository().getExisted(taskId);
        if (task.getParentId() != null) {
            throw new DataConflictException("Can't change subtask sprint");
        }
        if (sprintId != null) {
            Sprint sprint = sprintRepository.getExisted(sprintId);
            if (sprint.getProjectId() != task.getProjectId()) {
                throw new DataConflictException("Target sprint must belong to the same project");
            }
        }
        handler.getRepository().setTaskAndSubTasksSprint(taskId, sprintId);
    }

    @Transactional
    public Task create(TaskToExt taskTo) {
        Task created = handler.createWithBelong(taskTo, TASK, "task_author");
        activityHandler.create(makeActivity(created.id(), taskTo));
        return created;
    }

    @Transactional
    public void update(TaskToExt taskTo, long id) {
        if (!taskTo.equals(get(taskTo.id()))) {
            handler.updateFromTo(taskTo, id);
            activityHandler.create(makeActivity(id, taskTo));
        }
    }

    public TaskToFull get(long id) {
        Task task = Util.checkExist(id, handler.getRepository().findFullById(id));
        TaskToFull taskToFull = fullMapper.toTo(task);
        List<Activity> activities = activityHandler.getRepository().findAllByTaskIdOrderByUpdatedDesc(id);
        fillExtraFields(taskToFull, activities);
        taskToFull.setActivityTos(activityHandler.getMapper().toToList(activities));
        return taskToFull;
    }

    public TaskToExt getNewWithSprint(long sprintId) {
        Sprint sprint = sprintRepository.getExisted(sprintId);
        Task newTask = new Task();
        newTask.setSprintId(sprintId);
        newTask.setProjectId(sprint.getProjectId());
        return extMapper.toTo(newTask);
    }

    public TaskToExt getNewWithProject(long projectId) {
        Task newTask = new Task();
        newTask.setProjectId(projectId);
        return extMapper.toTo(newTask);
    }

    public TaskToExt getNewWithParent(long parentId) {
        Task parent = handler.getRepository().getExisted(parentId);
        Task newTask = new Task();
        newTask.setParentId(parentId);
        newTask.setSprintId(parent.getSprintId());
        newTask.setProjectId(parent.getProjectId());
        return extMapper.toTo(newTask);
    }

    public void assign(long id, String userType, long userId) {
        checkAssignmentActionPossible(id, userType, true);
        handler.createUserBelong(id, TASK, userId, userType);
    }

    @Transactional
    public void unAssign(long id, String userType, long userId) {
        checkAssignmentActionPossible(id, userType, false);
        UserBelong assignment = userBelongRepository.findActiveAssignment(id, TASK, userId, userType)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Not found assignment with userType=%s for task {%d} for user {%d}", userType, id, userId)));
        assignment.setEndpoint(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public Duration getTimeInProgress(long id) {
        return getTimeBetweenStatuses(id, TASK_STATUS_IN_PROGRESS, TASK_STATUS_READY_FOR_REVIEW);
    }

    @Transactional(readOnly = true)
    public Duration getTimeInTesting(long id) {
        return getTimeBetweenStatuses(id, TASK_STATUS_READY_FOR_REVIEW, TASK_STATUS_DONE);
    }

    private void checkAssignmentActionPossible(long id, String userType, boolean assign) {
        Assert.notNull(userType, "userType must not be null");
        Task task = handler.getRepository().getExisted(id);
        String possibleUserType = getRefTo(RefType.TASK_STATUS, task.getStatusCode()).getAux(1);
        if (!userType.equals(possibleUserType)) {
            throw new DataConflictException(String.format(assign ? CANNOT_ASSIGN : CANNOT_UN_ASSIGN, userType, task.getStatusCode()));
        }
    }

    private Duration getTimeBetweenStatuses(long id, String statusFrom, String statusTo) {
        Optional<LocalDateTime> timeFrom = activityRepository.findByTaskIdAndStatusCodeOrderByUpdatedAsc(id, statusFrom).stream()
                .findFirst()
                .map(Activity::getUpdated);
        if (timeFrom.isEmpty()) {
            return null;
        }

        Optional<LocalDateTime> timeTo = activityRepository.findByTaskIdAndStatusCodeAndUpdatedAfterOrderByUpdatedAsc(id, statusTo, timeFrom.get()).stream()
                .findFirst()
                .map(Activity::getUpdated);

        return timeTo.map(localDateTime -> Duration.between(timeFrom.get(), localDateTime)).orElse(null);
    }

    @Transactional(readOnly = true)
    public Set<String> getTaskTags(Long taskId) {
        return taskRepository.getTaskTags(taskId);
    }

    @Transactional
    public Set<String> addTagToTask(Long taskId, String tag) {
        validateTag(tag);
        Task task = handler.get(taskId);

        if (task != null && task.getTags().contains(tag)) {
            log.warn("Tag '{}' already exists for task {}", tag, taskId);
            return new HashSet<>(task.getTags());
        }

        task.getTags().add(tag);
        handler.update(task, taskId);
        log.info("Added tag '{}' to task {}", tag, taskId);
        return new HashSet<>(task.getTags());
    }

    @Transactional
    public Set<String> addTagsToTask(Long taskId, Set<String> tags) {
        validateTags(tags);
        Task task = handler.get(taskId);

        Set<String> existingTags = task.getTags();
        Set<String> tagsToAdd = tags.stream()
                .filter(tag -> !existingTags.contains(tag))
                .collect(Collectors.toSet());

        if (tagsToAdd.isEmpty()) {
            log.warn("No new tags to add for task {}", taskId);
            return new HashSet<>(existingTags);
        }

        existingTags.addAll(tagsToAdd);
        handler.update(task, taskId);
        log.info("Added tags {} to task {}", tagsToAdd, taskId);
        return new HashSet<>(existingTags);
    }

    @Transactional
    public Set<String> removeTagFromTask(Long taskId, String tag) {
        Task task = handler.get(taskId);

        if (!task.getTags().contains(tag)) {
            log.warn("Tag '{}' not found for task {}", tag, taskId);
            throw new NotFoundException("Tag '" + tag + "' not found for task " + taskId);
        }

        task.getTags().remove(tag);
        handler.update(task, taskId);
        log.info("Removed tag '{}' from task {}", tag, taskId);
        return new HashSet<>(task.getTags());
    }

    @Transactional
    public Set<String> removeTagsFromTask(Long taskId, Set<String> tags) {
        validateTags(tags);
        Task task = handler.get(taskId);

        Set<String> existingTags = task.getTags();
        Set<String> tagsToRemove = tags.stream()
                .filter(existingTags::contains)
                .collect(Collectors.toSet());

        if (tagsToRemove.isEmpty()) {
            log.warn("No tags to remove for task {}", taskId);
            return new HashSet<>(existingTags);
        }

        existingTags.removeAll(tagsToRemove);
        handler.update(task, taskId);
        log.info("Removed tags {} from task {}", tagsToRemove, taskId);
        return new HashSet<>(existingTags);
    }

    @Transactional
    public Set<String> replaceTaskTags(Long taskId, Set<String> newTags) {
        validateTags(newTags);
        Task task = handler.get(taskId);

        task.setTags(newTags);
        handler.update(task, taskId);
        log.info("Replaced tags for task {} with {}", taskId, newTags);
        return new HashSet<>(newTags);
    }

    private void validateTag(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new ValidationException("Tag cannot be null or blank");
        }
        if (tag.length() < 2 || tag.length() > 32) {
            throw new ValidationException("Tag must be between 2 and 32 characters");
        }
    }

    private void validateTags(Set<String> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new ValidationException("Tags set cannot be null or empty");
        }
        tags.forEach(this::validateTag);
    }
}
