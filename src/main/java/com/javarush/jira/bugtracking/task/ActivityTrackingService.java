package com.javarush.jira.bugtracking.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//todo Добавить подсчет времени сколько задача находилась в работе и тестировании.
// Написать 2 метода на уровне сервиса, которые параметром принимают задачу и возвращают затраченное время:
//  Сколько задача находилась в работе (ready_for_review минус in_progress ).
//  Сколько задача находилась на тестировании (done минус ready_for_review).
//  Для написания этого задания, нужно добавить в конец скрипта инициализации базы данных init-database.sql 3 записи в таблицу ACTIVITY
//  insert into ACTIVITY ( ID, AUTHOR_ID, TASK_ID, UPDATED, STATUS_CODE ) values ...
//  Со статусами:
//  время начала работы над задачей – in_progress
//  время окончания разработки - ready_for_review
//  время конца тестирования - done

@Service
@RequiredArgsConstructor
public class ActivityTrackingService {
}
