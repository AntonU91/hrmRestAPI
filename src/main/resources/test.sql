DELETE FROM project;
DELETE FROM employee;
DELETE FROM project_employee;
DELETE FROM project_manager;
ALTER TABLE hibernate_sequence ALTER COLUMN next_val RESTART WITH 1;
ALTER TABLE  employee ALTER COLUMN id RESTART WITH 1;
ALTER TABLE  project_manager ALTER COLUMN id RESTART WITH 1;



create  table if not exists employee
(
    id               int        not null
        primary key,
    experience_level varchar(255) not null,
    hired_at         date         not null,
    name             varchar(25)  not null,
    position         varchar(255) not null,
    surname          varchar(25)  not null
);
create table  if not exists project_manager
(
    id       int     not null
        primary key,
    hired_at date        not null,
    name     varchar(25) not null,
    surname  varchar(25) not null
);
create table  if not exists project
(
    id                 int      not null
        primary key,
    launched_at        date        null,
    name               varchar(25) not null,
    project_manager_id int         null,
    constraint FKg4751fh0rnvsu3apqlwfxs4hp
        foreign key (project_manager_id) references project_manager (id)
);

create table  if not exists project_employee
(
    project_id  int not null,
    employee_id int not null,
    primary key (employee_id, project_id),
    constraint FK1907nkisp2dlsswuycpnakiv8
        foreign key (project_id) references project (id),
    constraint FKn5yqs0xm3rmsg62n84ccyk4k0
        foreign key (employee_id) references employee (id)
);
create table  if not exists hibernate_sequence
(
    next_val bigint null
);





INSERT INTO employee (id, experience_level, hired_at, name, position, surname) VALUES (2, 'MIDDLE', '2023-04-02', 'Anton', 'DEVELOPER', 'Uzhva');
INSERT INTO employee (id, experience_level, hired_at, name, position, surname) VALUES (3, 'JUNIOR', '2023-04-01', 'Alisa', 'DEVELOPER', 'Karpenko');
INSERT INTO employee (id, experience_level, hired_at, name, position, surname) VALUES (4, 'MIDDLE', '2023-04-03', 'Valik', 'QA', 'Krishenko');

INSERT INTO project (id, launched_at, name, project_manager_id) VALUES (2, '2023-04-02', 'First project', null);
INSERT INTO project (id, launched_at, name, project_manager_id) VALUES (13, '2023-04-03', 'Second Proj', null);
INSERT INTO project (id, launched_at, name, project_manager_id) VALUES (14, '2023-04-03', 'Third', null);

INSERT INTO project_manager (id, hired_at, name, surname) VALUES (2, '2023-04-05', 'Bogdan', 'Gorbach');
INSERT INTO project_manager (id, hired_at, name, surname) VALUES (3, '2023-04-01', 'Svitlana', 'Zarevich');


