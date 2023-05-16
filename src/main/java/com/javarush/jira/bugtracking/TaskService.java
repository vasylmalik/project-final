package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Activity;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.ActivityRepository;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.to.TaskTo;
import com.javarush.jira.common.error.IllegalRequestDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {

    @Autowired
    private ActivityRepository activityRepository;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        super(repository, mapper);
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    public void addTag(String tag, long taskId) {
        Task task = repository.getExisted(taskId);
        if (task.getTags().contains(tag)) {
            throw new IllegalRequestDataException(String.format("Task with id = %d already has '%s' tag.", taskId, tag));
        }
        task.getTags().add(tag);
        repository.save(task);
    }

    public void addTags(Set<String> tags, long taskId) {
        Task task = repository.getExisted(taskId);
        Set<String> intersectionTags = new HashSet<>(tags);
        intersectionTags.retainAll(task.getTags());
        if (!intersectionTags.isEmpty()) {
            throw new IllegalRequestDataException(String.format("Task with id = %d already has '%s' tags.", taskId, intersectionTags));
        }
        task.getTags().addAll(tags);
        repository.save(task);
    }

    public long takeMsInProgress(Task task) {
        List<Activity> activityList = activityRepository.findAllByTaskOrderByUpdatedDesc(task);
        return TaskUtil.takeMsInProgress(activityList);
    }

    public long takeMsInTesting(Task task) {
        List<Activity> activityList = activityRepository.findAllByTaskOrderByUpdatedDesc(task);
        return TaskUtil.takeMsInTesting(activityList);
    }

}
