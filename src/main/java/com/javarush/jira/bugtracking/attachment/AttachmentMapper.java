package com.javarush.jira.bugtracking.attachment;

import com.javarush.jira.bugtracking.attachment.to.AttachmentTo;
import com.javarush.jira.common.BaseMapper;
import com.javarush.jira.common.TimestampMapper;
import org.mapstruct.Mapper;

@Mapper(config = TimestampMapper.class)
public interface AttachmentMapper extends BaseMapper<Attachment, AttachmentTo> {
}
