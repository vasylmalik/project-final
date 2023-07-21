package com.javarush.jira.mail.internal;

import com.javarush.jira.common.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MailCaseRepository extends BaseRepository<MailCase> {
}
