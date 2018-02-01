create database crawler;
grant all privileges on crawler.* to crawler identified by 'crawler_pass';

create database crawler_rpl;
grant all privileges on crawler_rpl.* to crawler identified by 'crawler_pass';

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
) engine = InnoDB default character set utf8;

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
) engine = InnoDB default character set utf8;
alter table novel_history add index id_novel_id(id, novel_id);
alter table novel_history add index novel_id(novel_id);

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
) engine = InnoDB default character set utf8;
alter table novel_chapter add index id_novel_id(id, novel_id);
alter table novel_chapter add index novel_id(novel_id);

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
) engine = InnoDB default character set utf8;
alter table novel_chapter_history add index id_novel_chapter_id(id, novel_chapter_id);
alter table novel_chapter_history add index novel_chapter_id(novel_chapter_id);

create table novel_info (
    id integer not null auto_increment,
    novel_id integer not null,
    checked_date datetime default null,
    modified_date datetime default null,
    finished boolean default false,
    keyword varchar(300) default null,
    favorite boolean default null,
    rank integer default null,
    check_enable boolean default null,
    version integer default null,
    create_user varchar(16) default null,
    create_date timestamp default '0000-00-00 00:00:00',
    update_user varchar(16) default null,
    update_date timestamp default '0000-00-00 00:00:00',
    primary key (id)
) engine = InnoDB default character set utf8;
alter table novel_info add index id_novel_id(id, novel_id);
alter table novel_info add index novel_id(novel_id);
alter table novel_info add index checked_date(checked_date);

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
) engine = InnoDB default character set utf8;
alter table novel_chapter_info add index id_novel_chapter_id(id, novel_chapter_id);
alter table novel_chapter_info add index novel_chapter_id(novel_chapter_id);
