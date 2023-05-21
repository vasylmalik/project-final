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
    public TaskService(TaskRepository repository, TaskMapper mapper,
                       UserRepository userRepository,
                       UserBelongRepository userBelongRepository) {

        super(repository, mapper);
        this.userRepository = userRepository;
        this.userBelongRepository = userBelongRepository;
    }

    public List<TaskTo> getAll() {
        return mapper.toToList(repository.getAll());
    }

    public void addTagToTask(Long id, Set<String> tags) {
        Task task = repository.getExisted(id);
        task.getTags().addAll(tags);
        repository.save(task);
    }

    public void addTaskToUser(Long taskId, Long userId) {
        UserBelong userBelong = new UserBelong();
        User user = userRepository.getExisted(userId);

        userBelong.setUserId(user.id());
        userBelong.setObjectId(taskId);
        userBelong.setObjectType(ObjectType.TASK);
        userBelongRepository.save(userBelong);
    }
}