package com.javarush.jira.bugtracking.project;

import com.javarush.jira.common.TimestampRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ProjectRepository extends TimestampRepository<Project> {
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.parent WHERE p.id =:id")
    Optional<Project> findFullById(long id);
}
