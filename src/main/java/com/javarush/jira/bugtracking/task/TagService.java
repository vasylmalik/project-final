package com.javarush.jira.bugtracking.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TaskRepository taskRepository;

    @Transactional
    public Set<String> getTags(long id) {
        log.info("Getting existed tags for task id {}", id);
        Task task = taskRepository.getTaskByIdWithTags(id);

        Set<String> tags = task.getTags();
        log.info("Retrieved {} tags for task id: {}", tags.size(), id);

        return tags;
    }

    @Transactional
    public Task addTags(long id, Set<String> tags) {
        log.info("Adding tags for task id {}", id);
        Optional<Task> optionalTask = taskRepository.findFullById(id);

        Task task = getTask(id, optionalTask);

        try {
            task.getTags().addAll(tags);
            taskRepository.save(task);
            log.info("Tags for task id {} have been added", id);

            return task;
        } catch (Exception e) {
            log.error("Error adding tags for task id {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Task replaceTags(long id, Set<String> tags) {
        log.info("Replacing existed tags for task id {}", id);
        Optional<Task> optionalTask = taskRepository.findFullById(id);

        Task task = getTask(id, optionalTask);

        try {
            task.setTags(tags);
            taskRepository.save(task);
            log.info("Tags for task id {} successfully replaced with new tags", id);

            return task;
        } catch (Exception e) {
            log.error("Error replacing tags for task id {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void removeAllTags(long id) {
        log.info("Removing tags for task id {}", id);
        Optional<Task> optionalTask = taskRepository.findFullById(id);
        Task task = getTask(id, optionalTask);

        try {

            task.getTags().clear();
            taskRepository.save(task);
            log.info("Tags for task id {} successfully removed", id);
        } catch (Exception e) {
            log.error("Error removing tags for task id {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void removeSingleTag(long id, String tag) {
        log.info("Removing single tag for task id {}", id);
        Optional<Task> optionalTask = taskRepository.findFullById(id);
        Task task = getTask(id, optionalTask);

        try {
            boolean tagRemoved = task.getTags().remove(tag);

            if (tagRemoved) {
                taskRepository.save(task);
                log.info("Tag {} for task id {} successfully removed", tag, id);
            } else {
                log.warn("Tag '{}' not found in task id {}", tag, id);
            }

        } catch (Exception e) {
            log.error("Error removing tag {} for task id {}: {}", tag, id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Task getTask(long id, Optional<Task> optionalTask) {
        return optionalTask.orElseThrow(() -> {
                    log.error("Task with id {} not found", id);
                    return new RuntimeException("Task with id " + id + " not found");
                }
        );
    }
}
