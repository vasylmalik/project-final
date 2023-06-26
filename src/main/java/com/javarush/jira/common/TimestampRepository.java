package com.javarush.jira.common;

import com.javarush.jira.common.model.TimestampEntry;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface TimestampRepository<T extends TimestampEntry> extends BaseRepository<T> {
    Sort NEWEST_FIRST = Sort.by(Sort.Direction.DESC, "startpoint");

    default List<T> getAll() {
        return findAll(NEWEST_FIRST);
    }

    @Query("SELECT te FROM #{#entityName} te WHERE te.endpoint IS NULL OR te.endpoint >=now()")
    List<T> getAllEnabled();
}
