package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.bugtracking.to.TaskTo;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {

    private final UserRepository userRepository;
    private final UserBelongRepository userBelongRepository;

    public TaskService(TaskRepository repository, TaskMapper mapper, UserRepository userRepository, UserBelongRepository userBelongRepository) {
        super(repository, mapper);
        this.userRepository = userRepository;
        this.userBelongRepository = userBelongRepository;
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    public void addTagsToTask(Long taskId, Set<String> tags) {
        Task task = repository.getExisted(taskId);
        task.getTags().addAll(tags);
        repository.save(task);
    }

    public void addUserToTask(Long taskId, Long userId) {
        Task task = repository.getExisted(taskId);
        User user = userRepository.getExisted(userId);

        UserBelong userBelongTask = new UserBelong();
        userBelongTask.setObjectId(task.getId());
        userBelongTask.setObjectType(ObjectType.TASK);
        userBelongTask.setUserId(user.getId());
        userBelongTask.setUserTypeCode("admin"); // where is this reference uses?

        userBelongRepository.save(userBelongTask);
    }

    public Page<TaskTo> getAllWhereSprintIsNull(int page, int size) { // TODO 12.add backlog
        return repository.findBySprintIsNull(PageRequest.of(page - 1, size))
                .map(mapper::toTo);
    }
}
