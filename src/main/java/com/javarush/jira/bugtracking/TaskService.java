package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.to.TaskTo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {
    public TaskService(TaskRepository repository, TaskMapper mapper) {
        super(repository, mapper);
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    // 1. if size = 0, validate 2/32; add tag
    // 2. if size != 0; check if exist; validate; add
    public void addTag(String tag, long taskId){
        Task task = repository.getExisted(taskId);
        Set<String> tags = task.getTags();
        if (tags.isEmpty() || !tags.contains(tag)){
            validateAndAdd(tag, 2, 32, tags);
            repository.save(task);
        }
    }

    public void validateAndAdd(String tag, int min, int max, Set<String> tags){
        if (tag.length()>=2 && tag.length()<=32){
            tags.add(tag);
        }
    }
}
