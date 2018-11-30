delete from user_role where user_id = 1;
delete from users where id = 1;

insert into users (id, username, password, active)
    values (1, 'admin', '123', true);

insert into user_role (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN');

commit;