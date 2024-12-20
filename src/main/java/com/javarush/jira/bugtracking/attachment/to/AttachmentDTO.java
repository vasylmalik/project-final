package com.javarush.jira.bugtracking.attachment.to;

import com.javarush.jira.common.to.NamedDTO;
import lombok.Getter;

@Getter
public class AttachmentDTO extends NamedDTO {
    public AttachmentDTO(Long id, String name) {
        super(id, name);
    }
}
