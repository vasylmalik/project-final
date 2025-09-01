package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.project.Project;
import com.javarush.jira.bugtracking.project.ProjectRepository;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ActivityServiceDurationTest {

    @Autowired private ActivityService activityService;
    @Autowired private TaskRepository taskRepository;
    @Autowired private ActivityRepository activityRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProjectRepository projectRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void calculatesWorkAndTestingDurations() {
        User author = userRepository.save(
                new User(null, "author@test.local", "{noop}pass",
                        "Author", "Tester", "author", Role.ADMIN)
        );

        Project p = projectRepository.save(
                new Project(
                        null,
                        "PRJ_TEST_" + System.nanoTime(),
                        "Test Project",
                        "software",
                        "Project for duration test",
                        null
                )
        );
        em.flush();
        assertThat(p.getId()).as("project id must be assigned").isNotNull();

        Task task = new Task();
        task.setTitle("Duration demo");
        task.setTypeCode("task");
        task.setStatusCode("done");
        task.setProjectId(p.getId());
        task = taskRepository.save(task);
        em.flush();

        LocalDateTime t1 = LocalDateTime.of(2025, 8, 20, 10, 0);
        LocalDateTime t2 = LocalDateTime.of(2025, 8, 21, 16, 30);
        LocalDateTime t3 = LocalDateTime.of(2025, 8, 22, 12, 15);

        Activity a1 = activityRepository.save(new Activity(
                null, author.getId(), task.getId(), t1,
                null, "in_progress", null, task.getTypeCode(),
                "Status: In progress", null, null
        ));

        Activity a2 = activityRepository.save(new Activity(
                null, author.getId(), task.getId(), t2,
                null, "ready_for_review", null, task.getTypeCode(),
                "Status: Ready for review", null, null
        ));

        Activity a3 = activityRepository.save(new Activity(
                null, author.getId(), task.getId(), t3,
                null, "done", null, task.getTypeCode(),
                "Status: Done", null, null
        ));
        em.flush();

        em.createNativeQuery("update activity set updated = ? where id = ?")
                .setParameter(1, Timestamp.valueOf(t1))
                .setParameter(2, a1.getId())
                .executeUpdate();
        em.createNativeQuery("update activity set updated = ? where id = ?")
                .setParameter(1, Timestamp.valueOf(t2))
                .setParameter(2, a2.getId())
                .executeUpdate();
        em.createNativeQuery("update activity set updated = ? where id = ?")
                .setParameter(1, Timestamp.valueOf(t3))
                .setParameter(2, a3.getId())
                .executeUpdate();
        em.flush();
        em.clear();

        Duration work = activityService.getTimeInWork(task);
        Duration testing = activityService.getTimeInTesting(task);

        assertThat(work).isEqualTo(Duration.ofHours(30).plusMinutes(30));   // 20.08 10:00 -> 21.08 16:30
        assertThat(testing).isEqualTo(Duration.ofHours(19).plusMinutes(45)); // 21.08 16:30 -> 22.08 12:15
    }
}
