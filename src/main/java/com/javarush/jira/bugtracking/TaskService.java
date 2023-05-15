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

    // 1. if size = 0, validate 2/32; add tag
    // 2. if size != 0; check if exist; validate; add

    //ADD TAG TO TASK
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

    //ASSIGN TASK TO USER
    public void assignTaskToUser(Long taskId, Long userId) {
        User user = userRepository.getExisted(userId);
        List<UserBelong> userBelongList = userBelongRepository.getByUserId(userId);
        if (userBelongList.isEmpty()){
            UserBelong userBelong = new UserBelong();
            userBelong.setUserId(user.id());
            userBelong.setObjectId(taskId);
            userBelong.setObjectType(ObjectType.TASK);
            userBelongRepository.save(userBelong);
        } else {
            for (int i = 0; i < userBelongList.size(); i++) {
                UserBelong userBelong = userBelongList.get(i);

                if((userBelong.getUserId() != userId) && (userBelong.getObjectId() != taskId)) {
                    userBelong.setUserId(user.id());
                    userBelong.setObjectId(taskId);
                    userBelong.setObjectType(ObjectType.TASK);
                    userBelongRepository.save(userBelong);
                }
            }
        }
    }
}
