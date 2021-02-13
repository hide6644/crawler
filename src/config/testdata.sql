INSERT INTO novel (id, url, title, writername, description, body) VALUES (-1, 'Url2', 'Title', 'Writername', 'Description', 'Body');
INSERT INTO novel_chapter (id, novel_id, url, title, body, create_date, update_date) VALUES (-1, -1, 'Url2', 'Title', 'Body', NOW(), NOW());
INSERT INTO novel_chapter_info (id, novel_chapter_id, checked_date, modified_date, unread) VALUES (-1, -1, DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY), 1);
INSERT INTO novel_info (id, novel_id, checked_date, modified_date, keyword, favorite, check_enable) VALUES (-1, -1, DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY), 'Keyword1 Keyword2', 0, 1);
