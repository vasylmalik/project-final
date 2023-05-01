package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.to.TaskTo;
import com.javarush.jira.common.error.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {
    public TaskService(TaskRepository repository, TaskMapper mapper) {
        super(repository, mapper);
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    public void saveTag(Long taskId,String tag){
        if(repository.existsById(taskId))
            repository.saveTag(taskId, tag);
        else
            throw new NotFoundException ("There is not task with id="+taskId);
    }

    public Task getTaskById(Long id){
        if(repository.existsById(id))
         return   repository.getTaskById(id);
        else
            throw new NotFoundException ("There is not task with id="+id);
    }
}
