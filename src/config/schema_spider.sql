create database crawler;
grant all privileges on crawler.* to crawler identified by 'crawler_pass';

drop server crawler_backend1;
drop server crawler_backend2;
drop server crawler_backend1_rpl;
drop server crawler_backend2_rpl;
drop server crawler_monitor1;
drop server crawler_monitor2;

create server crawler_backend1 foreign data wrapper mysql options (
    host '192.168.0.69',
    database 'crawler',
    user 'crawler',
    password 'crawler_pass',
    port 3306
);
create server crawler_backend2 foreign data wrapper mysql options (
    host '192.168.0.70',
    database 'crawler',
    user 'crawler',
    password 'crawler_pass',
    port 3306
);
create server crawler_backend1_rpl foreign data wrapper mysql options (
    host '192.168.0.69',
    database 'crawler_rpl',
    user 'crawler',
    password 'crawler_pass',
    port 3306
);
create server crawler_backend2_rpl foreign data wrapper mysql options (
    host '192.168.0.70',
    database 'crawler_rpl',
    user 'crawler',
    password 'crawler_pass',
    port 3306
);
create server crawler_monitor1 foreign data wrapper mysql options (
    host '192.168.0.67',
    database 'crawler',
    user 'crawler',
    password 'crawler_pass',
    port 3306
);
create server crawler_monitor2 foreign data wrapper mysql options (
    host '192.168.0.68',
    database 'crawler',
    user 'crawler',
    password 'crawler_pass',
    port 3306
);
insert into mysql.spider_link_mon_servers
    (db_name, table_name, link_id, sid, server)
values
    ('crawler%','%','%',1,'crawler_monitor1'),
    ('crawler%','%','%',2,'crawler_monitor2');
select spider_flush_table_mon_cache();

drop table if exists novel_chapter_info cascade;
drop table if exists novel_info cascade;
drop table if exists novel_chapter_history cascade;
drop table if exists novel_chapter cascade;
drop table if exists novel_history cascade;
drop table if exists novel cascade;

create table novel (
    id integer not null auto_increment,
    url varchar(100) not null,
    title varchar(100) default null,
    writername varchar(100) default null,
    description text default null,
    body mediumtext default null,
    deleted boolean default false,
    version integer default null,
    create_user varchar(16) default null,
    create_date timestamp default '0000-00-00 00:00:00',
    update_user varchar(16) default null,
    update_date timestamp default '0000-00-00 00:00:00',
    primary key (id)
) default character set utf8 engine=spider comment='wrapper "mysql", table "novel"'
    partition by key (id)
(
    partition pt1 comment = 'srv "crawler_backend1 crawler_backend2_rpl", mbk "2", mkd "2", link_status "1 1"',
    partition pt2 comment = 'srv "crawler_backend2 crawler_backend1_rpl", mbk "2", mkd "2", link_status "1 1" '
);

create table novel_history (
    id integer not null auto_increment,
    novel_id integer not null,
    title varchar(100) default null,
    writername varchar(100) default null,
    description text default null,
    body mediumtext default null,
    version integer default null,
    create_user varchar(16) default null,
    create_date timestamp default '0000-00-00 00:00:00',
    update_user varchar(16) default null,
    update_date timestamp default '0000-00-00 00:00:00',
    primary key (id)
) default character set utf8 engine=spider comment='wrapper "mysql", table "novel_history"'
    partition by key (id)
(
    partition pt1 comment = 'srv "crawler_backend1 crawler_backend2_rpl", mbk "2", mkd "2", link_status "1 1"',
    partition pt2 comment = 'srv "crawler_backend2 crawler_backend1_rpl", mbk "2", mkd "2", link_status "1 1" '
);

create table novel_chapter (
    id integer not null auto_increment,
    novel_id integer not null,
    url varchar(100) not null,
    title varchar(100) default null,
    body mediumtext default null,
    version integer default null,
    create_user varchar(16) default null,
    create_date timestamp default '0000-00-00 00:00:00',
    update_user varchar(16) default null,
    update_date timestamp default '0000-00-00 00:00:00',
    primary key (id)
) default character set utf8 engine=spider comment='wrapper "mysql", table "novel_chapter"'
    partition by key (id)
(
    partition pt1 comment = 'srv "crawler_backend1 crawler_backend2_rpl", mbk "2", mkd "2", link_status "1 1"',
    partition pt2 comment = 'srv "crawler_backend2 crawler_backend1_rpl", mbk "2", mkd "2", link_status "1 1" '
);

create table novel_chapter_history (
    id integer not null auto_increment,
    novel_chapter_id integer not null,
    title varchar(100) default null,
    body mediumtext default null,
    version integer default null,
    create_user varchar(16) default null,
    create_date timestamp default '0000-00-00 00:00:00',
    update_user varchar(16) default null,
    update_date timestamp default '0000-00-00 00:00:00',
    primary key (id)
) default character set utf8 engine=spider comment='wrapper "mysql", table "novel_chapter_history"'
    partition by key (id)
(
    partition pt1 comment = 'srv "crawler_backend1 crawler_backend2_rpl", mbk "2", mkd "2", link_status "1 1"',
    partition pt2 comment = 'srv "crawler_backend2 crawler_backend1_rpl", mbk "2", mkd "2", link_status "1 1" '
);

create table novel_info (
    id integer not null auto_increment,
    novel_id integer not null,
    checked_date datetime default null,
    modified_date datetime default null,
    finished boolean default false,
    keyword varchar(300) default null,
    favorite boolean default null,
    rank integer default null,
    version integer default null,
    create_user varchar(16) default null,
    create_date timestamp default '0000-00-00 00:00:00',
    update_user varchar(16) default null,
    update_date timestamp default '0000-00-00 00:00:00',
    primary key (id)
) default character set utf8 engine=spider comment='wrapper "mysql", table "novel_info"'
    partition by key (id)
(
    partition pt1 comment = 'srv "crawler_backend1 crawler_backend2_rpl", mbk "2", mkd "2", link_status "1 1"',
    partition pt2 comment = 'srv "crawler_backend2 crawler_backend1_rpl", mbk "2", mkd "2", link_status "1 1" '
);

create table novel_chapter_info (
    id integer not null auto_increment,
    novel_chapter_id integer not null,
    checked_date datetime default null,
    modified_date datetime default null,
    unread boolean default null,
    read_date datetime default null,
    version integer default null,
    create_user varchar(16) default null,
    create_date timestamp default '0000-00-00 00:00:00',
    update_user varchar(16) default null,
    update_date timestamp default '0000-00-00 00:00:00',
    primary key (id)
) default character set utf8 engine=spider comment='wrapper "mysql", table "novel_chapter_info"'
    partition by key (id)
(
    partition pt1 comment = 'srv "crawler_backend1 crawler_backend2_rpl", mbk "2", mkd "2", link_status "1 1"',
    partition pt2 comment = 'srv "crawler_backend2 crawler_backend1_rpl", mbk "2", mkd "2", link_status "1 1" '
);
