package com.lazareff.taskmanager.repository;

import com.lazareff.taskmanager.entity.Task;
import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByUser(User user);

    List<Task> findAllByUserId(Long userId);

    List<Task> findAllByStatus(TaskStatus status);

    List<Task> findAllByUserIdAndStatus(Long userId, TaskStatus status);

    Optional<Task> findByIdAndUser(Long id, User user);

    List<Task> findAllByUserAndStatus(User user, TaskStatus status);

    Page<Task> findAllByUser(User user, Pageable pageable);

}
