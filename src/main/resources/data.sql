INSERT INTO user(id, username, password) VALUES
    (1, 'domin', '{noop}wow'),
    (2, 'klaula', '{noop}lel'),
    (3, 'makso', '{noop}heh'),
    (4, 'wowo', '{noop}lol'),
    (5, 'admin', '{noop}admin');


    INSERT INTO user_role(user_id, role) VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_USER'),
    (4, 'ROLE_USER'),
    (5, 'ROLE_ADMIN');