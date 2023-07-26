INSERT INTO activity (id, author_id, task_id, updated, status_code)
VALUES (7, 2, 44, NOW() - INTERVAL '3 DAY', 'in_progress'),
       (8, 2, 44, NOW() - INTERVAL '2 DAY', 'ready_for_review'),
       (9, 2, 44, NOW() - INTERVAL '1 DAY', 'done');