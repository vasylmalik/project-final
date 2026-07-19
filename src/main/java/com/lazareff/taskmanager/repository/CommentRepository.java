package com.lazareff.taskmanager.repository;

import com.lazareff.taskmanager.entity.Comment;
import com.lazareff.taskmanager.entity.User;
import com.lazareff.taskmanager.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTaskId(Long taskId);

    Optional<Comment> findByIdAndUser(Long id, User user);

}
