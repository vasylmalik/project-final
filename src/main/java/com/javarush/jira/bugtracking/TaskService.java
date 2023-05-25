package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.internal.mapper.TaskMapper;
import com.javarush.jira.bugtracking.internal.model.Task;
import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.internal.repository.TaskRepository;
import com.javarush.jira.bugtracking.internal.repository.UserBelongRepository;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.bugtracking.to.TaskTo;
import com.javarush.jira.common.error.WrongTagException;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TaskService extends BugtrackingService<Task, TaskTo, TaskRepository> {
    private final UserRepository userRepository;

    private final UserBelongRepository userBelongRepository;

    private final int minLength = 2;
    private final int maxLength = 32;

    public TaskService(TaskRepository repository, TaskMapper mapper, UserRepository userRepository, UserBelongRepository userBelongRepository) {
        super(repository, mapper);
        this.userRepository = userRepository;
        this.userBelongRepository = userBelongRepository;
    }
    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    //ADD TAG TO TASK
    public void addTag(String tag, long taskId){
        Task task = repository.getExisted(taskId);
        Set<String> tags = task.getTags();
        if (tags.isEmpty() || !tags.contains(tag)){
            validateAndAdd(tag, minLength, maxLength, tags);
            repository.save(task);
        }
    }

    //VALIDATE IF THE TAG LENGTH IS CORRECT
    private void validateAndAdd(String tag, int min, int max, Set<String> tags){
        if (tag.length()>=min && tag.length()<=max){
            tags.add(tag);
        } else throw new WrongTagException("Tag character number should be more than 2 but less than 32");
    }

    //ASSIGN TASK TO USER IF NOT ASSIGNED
    public void assignTaskToUser(Long taskId, Long userId) {
        User user = userRepository.getExisted(userId);
        //get userBelong list for the current user
        List<UserBelong> userBelongList = userBelongRepository.getByUserId(userId);
        if (userBelongList.isEmpty()){
            UserBelong userBelong = new UserBelong();
            // if the list is empty create new userBelong, assign the task to the current user
            // and save the userBelong to the DB
            saveUserBelongParameters(userBelong, user, taskId);
        } else {
            for (int i = 0; i < userBelongList.size(); i++) {
                UserBelong userBelong = userBelongList.get(i);
                //check each element of the userBelong List

                if((userBelong.getUserId() != userId) && (userBelong.getObjectId() != taskId)) {
                    // if current task is not assigned to the current user then assign the task
                    // and save the userBelong to the DB
                    saveUserBelongParameters(userBelong, user, taskId);
                }
            }
        }
    }

    @Transactional
    private void saveUserBelongParameters(UserBelong userBelong, User user, Long taskId){
        userBelong.setUserId(user.id());
        userBelong.setObjectId(taskId);
        userBelong.setObjectType(ObjectType.TASK);
        userBelongRepository.save(userBelong);
    }
}
