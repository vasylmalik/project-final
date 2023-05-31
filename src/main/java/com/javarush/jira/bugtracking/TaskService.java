package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.to.TaskTo;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {
    public TaskService(TaskRepository repository, TaskMapper mapper) {
        super(repository, mapper);
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    public void addTags(Long id, Set<String> tags) {
        int maxSizeTag = 32;
        int minSizeTag = 2;

        Task task = repository.getExisted(id);
        Set<String> curTags = task.getTags();
        for (String tag : tags) {
            if (curTags.contains(tag)) continue;
            validateSize(tag, minSizeTag, maxSizeTag);
            curTags.add(tag);
        }
        repository.save(task);
        log.info("Tags have been added to the task with id={}", id);
    }

    private void validateSize(String tags, int min, int max) {
        if (tags.length() < min || tags.length() > max) {
            throw new ValidationException(String.format("Tag size can't be less %d or more %d", min, max));
        }
    }
}
