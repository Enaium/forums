create table category
(
    id          int         not null
        primary key,
    title       varchar(10) not null,
    description varchar(50) not null
);

create table member
(
    id        bigint auto_increment comment 'Member id'
        primary key,
    username  varchar(10) null comment 'Member Username',
    password  varchar(32) null comment 'Member Password',
    join_time datetime    null comment 'Member Join Time'
);

create table member_info
(
    id          bigint                            not null comment 'Member id'
        primary key,
    avatar      varchar(255) default ''           null,
    description varchar(255) default ''           null,
    birthday    date         default '9999-09-09' null,
    gender      char         default ''           null,
    nickname    varchar(20)  default ''           not null
);

create table member_role
(
    id   tinyint     not null
        primary key,
    name varchar(10) not null
);

create table member_permission
(
    id    bigint            not null
        primary key,
    level tinyint default 1 not null,
    role  tinyint default 3 not null,
    constraint member_permission_member_role_id_fk
        foreign key (role) references member_role (id)
);

create table post
(
    id          bigint auto_increment
        primary key,
    title       varchar(50) not null,
    content     text        not null,
    category    int         not null,
    post_time   datetime    not null,
    `from`      bigint      not null,
    update_time datetime    not null,
    constraint post_category_id_fk
        foreign key (category) references category (id),
    constraint post_member_id_fk
        foreign key (`from`) references member (id)
);

create table post_comment
(
    id           bigint auto_increment
        primary key,
    post_id      bigint               not null,
    member_id    bigint               not null,
    comment_time datetime             not null,
    edited       tinyint(1) default 0 null,
    deleted      tinyint(1) default 0 null,
    comment      varchar(255)         not null
);


