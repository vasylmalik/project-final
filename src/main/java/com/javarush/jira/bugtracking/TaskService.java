package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Activity;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.ActivityRepository;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.bugtracking.to.TaskTo;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.javarush.jira.common.util.Util.getFormattedDuration;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {
    public static final String STATUS_IN_PROGRESS = "in progress";
    public static final String STATUS_IN_TEST = "in test";
    public static final String STATUS_DONE = "done";


    private final UserRepository userRepository;
    private final UserBelongRepository userBelongRepository;
    private final ActivityRepository activityRepository;

    public TaskService(TaskRepository repository, TaskMapper mapper, UserRepository userRepository, UserBelongRepository userBelongRepository, ActivityRepository activityRepository) {
        super(repository, mapper);
        this.userRepository = userRepository;
        this.userBelongRepository = userBelongRepository;
        this.activityRepository = activityRepository;
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    public void addTagsToTask(Long taskId, Set<String> tags) { //TODO: 6. Add feature new tags
        Task task = repository.getExisted(taskId);
        task.getTags().addAll(tags);
        repository.save(task);
    }

    public void addUserToTask(Long taskId, Long userId) { //TODO: 7. Add subscribe feature
        Task task = repository.getExisted(taskId);
        User user = userRepository.getExisted(userId);

        UserBelong userBelongTask = new UserBelong();
        userBelongTask.setObjectId(task.getId());
        userBelongTask.setObjectType(ObjectType.TASK);
        userBelongTask.setUserId(user.getId());
        userBelongTask.setUserTypeCode("admin"); // where is this reference uses?

        Optional<UserBelong> belong = userBelongRepository.findOne(Example.of(userBelongTask));
        if (belong.isEmpty()) {
            userBelongRepository.save(userBelongTask);
        }
    }

    public Page<TaskTo> getAllWhereSprintIsNull(int page, int size) { //TODO: 12.add backlog
        return repository.findBySprintIsNull(PageRequest.of(page - 1, size))
                .map(mapper::toTo);
    }

    public Map<String, String> getTaskSummary(Long taskId) { //TODO: 8.add task summary
        Task task = repository.getExisted(taskId);
        Map<String, Optional<LocalDateTime>> collect = activityRepository.findByTaskAndUpdatedNotNullAndStatusCodeNotNull(task).stream()
                .collect(Collectors.groupingBy(Activity::getStatusCode)).entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().stream()
                        .map(Activity::getUpdated)
                        .min(Comparator.naturalOrder())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, String> summary = new HashMap<>();
        Optional<LocalDateTime> progress = collect.get(STATUS_IN_PROGRESS) == null ? Optional.empty() : collect.get(STATUS_IN_PROGRESS);
        Optional<LocalDateTime> test = collect.get(STATUS_IN_TEST) == null ? Optional.empty() : collect.get(STATUS_IN_TEST);
        Optional<LocalDateTime> done = collect.get(STATUS_DONE) == null ? Optional.empty() : collect.get(STATUS_DONE);

        if(done.isPresent() && test.isPresent()) {
            summary.put(STATUS_IN_TEST, getFormattedDuration(test.get(), done.get()));
        }
        if(test.isPresent() && progress.isPresent()) {
            summary.put(STATUS_IN_PROGRESS, getFormattedDuration(progress.get(), test.get()));
        }
        return summary;
    }
}
