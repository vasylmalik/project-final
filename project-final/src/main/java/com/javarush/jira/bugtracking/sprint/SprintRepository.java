package com.javarush.jira.bugtracking.sprint;

import com.javarush.jira.common.TimestampRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface SprintRepository extends TimestampRepository<Sprint> {
    @Query("SELECT s FROM Sprint s WHERE s.project.id=:projectId AND s.statusCode=:statusCode ORDER BY s.startpoint DESC")
    List<Sprint> getAllByProjectAndStatus(long projectId, String statusCode);

    @Query("SELECT s FROM Sprint s WHERE s.project.id=:projectId ORDER BY s.startpoint DESC")
    List<Sprint> getAllByProject(long projectId);

    @Query("SELECT s FROM Sprint s WHERE s.project.id=:projectId AND (s.endpoint IS NULL OR s.endpoint >=now()) ORDER BY s.startpoint DESC")
    List<Sprint> getAllEnabledByProject(long projectId);

    @Query("SELECT s FROM Sprint s JOIN FETCH s.project WHERE s.id =:id")
    Optional<Sprint> findFullById(long id);
}
