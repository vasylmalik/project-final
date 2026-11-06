package com.javarush.jira.bugtracking;

import com.javarush.jira.bugtracking.attachment.Attachment;
import com.javarush.jira.bugtracking.attachment.AttachmentMapper;
import com.javarush.jira.bugtracking.attachment.AttachmentRepository;
import com.javarush.jira.bugtracking.attachment.to.AttachmentTo;
import com.javarush.jira.bugtracking.project.Project;
import com.javarush.jira.bugtracking.project.ProjectMapper;
import com.javarush.jira.bugtracking.project.ProjectRepository;
import com.javarush.jira.bugtracking.project.to.ProjectTo;
import com.javarush.jira.bugtracking.sprint.Sprint;
import com.javarush.jira.bugtracking.sprint.SprintMapper;
import com.javarush.jira.bugtracking.sprint.SprintRepository;
import com.javarush.jira.bugtracking.sprint.to.SprintTo;
import com.javarush.jira.bugtracking.task.Activity;
import com.javarush.jira.bugtracking.task.ActivityRepository;
import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.bugtracking.task.TaskRepository;
import com.javarush.jira.bugtracking.task.mapper.ActivityMapper;
import com.javarush.jira.bugtracking.task.mapper.TaskExtMapper;
import com.javarush.jira.bugtracking.task.mapper.TaskFullMapper;
import com.javarush.jira.bugtracking.task.mapper.TaskMapper;
import com.javarush.jira.bugtracking.task.to.ActivityTo;
import com.javarush.jira.bugtracking.task.to.TaskTo;
import com.javarush.jira.bugtracking.task.to.TaskToExt;
import com.javarush.jira.bugtracking.task.to.TaskToFull;
import com.javarush.jira.common.BaseHandler;
import com.javarush.jira.common.BaseMapper;
import com.javarush.jira.common.BaseRepository;
import com.javarush.jira.common.HasId;
import com.javarush.jira.common.to.BaseTo;
import com.javarush.jira.login.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Handlers {
    @Component
    public static class ProjectHandler extends UserBelongHandler<Project, ProjectTo, ProjectRepository, ProjectMapper> {
        public ProjectHandler(ProjectRepository repository, ProjectMapper mapper) {
            super(repository, mapper);
        }
    }

    @Component
    public static class SprintHandler extends UserBelongHandler<Sprint, SprintTo, SprintRepository, SprintMapper> {
        public SprintHandler(SprintRepository repository, SprintMapper mapper) {
            super(repository, mapper, null, (sprint, dbSprint) -> {  // link spring to other project not allowed
                SprintMapper.checkProjectBelong(sprint.getProjectId(), dbSprint);
                sprint.setStartpoint(dbSprint.getStartpoint());
                sprint.setEndpoint(dbSprint.getEndpoint());
                return sprint;
            });
        }
    }

    @Component
    public static class TaskHandler extends UserBelongHandler<Task, TaskTo, TaskRepository, TaskMapper> {
        public TaskHandler(TaskRepository repository, TaskMapper mapper) {
            super(repository, mapper);
        }
    }

    @Component
    public static class TaskFullHandler extends UserBelongHandler<Task, TaskToFull, TaskRepository, TaskFullMapper> {
        public TaskFullHandler(TaskRepository repository, TaskFullMapper mapper) {
            super(repository, mapper);
        }
    }

    @Component
    public static class TaskExtHandler extends UserBelongHandler<Task, TaskToExt, TaskRepository, TaskExtMapper> {
        public TaskExtHandler(TaskRepository repository, TaskExtMapper mapper) {
            super(repository, mapper);
        }
    }

    @Component
    public static class ActivityHandler extends BaseHandler<Activity, ActivityTo, ActivityRepository, ActivityMapper> {
        public ActivityHandler(ActivityRepository repository, ActivityMapper mapper) {
            super(repository, mapper);
        }
    }

    @Component
    public static class AttachmentHandler extends BaseHandler<Attachment, AttachmentTo, AttachmentRepository, AttachmentMapper> {
        public AttachmentHandler(AttachmentRepository repository, AttachmentMapper mapper) {
            super(repository, mapper);
        }
    }

    @Slf4j
    public static class UserBelongHandler<E extends HasId, T extends BaseTo, R extends BaseRepository<E>, M extends BaseMapper<E, T>> extends BaseHandler<E, T, R, M> {
        @Autowired
        private UserBelongRepository belongRepository;

        public UserBelongHandler(R repository, M mapper) {
            super(repository, mapper);
        }

        public UserBelongHandler(R repository, M mapper, Function<E, E> prepareForSave, BiFunction<E, E, E> prepareForUpdate) {
            super(repository, mapper, prepareForSave, prepareForUpdate);
        }

        public List<UserBelong> getAllBelongs(long objectId) {
            return belongRepository.findAll(objectId);
        }

        @Transactional
        public E createWithBelong(T to, ObjectType type, String userTypeCode) {
            E created = super.createFromTo(to);
            createUserBelong(created.id(), type, AuthUser.authId(), userTypeCode);
            return created;
        }

        @Transactional
        public void createUserBelong(long id, ObjectType type, long userId, String userTypeCode) {
            if (belongRepository.findActiveAssignment(id, type, userId, userTypeCode).isEmpty()) {
                UserBelong belong = new UserBelong(id, type, userId, userTypeCode);
                belongRepository.save(belong);
            }
        }
    }
}
