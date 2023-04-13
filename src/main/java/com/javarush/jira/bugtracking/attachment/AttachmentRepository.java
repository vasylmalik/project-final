package com.javarush.jira.bugtracking.attachment;

import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AttachmentRepository extends BaseRepository<Attachment> {
    @Query("SELECT a FROM Attachment a WHERE a.objectType =:objectType")
    List<Attachment> getAllByObjectType(ObjectType objectType);

    @Query("SELECT a FROM Attachment a WHERE a.objectId =:objectId")
    List<Attachment> getAllByObjectId(Long objectId);
}
