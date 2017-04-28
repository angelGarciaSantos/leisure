-- INSERT INTO table_name (column1,column2,column3,...)
-- VALUES (value1,value2,value3,...);

INSERT INTO Local (id, name, description, capacity, rating)
VALUES (1, "Bar Pepe", "Bar de copas", 40, 7);

INSERT INTO Local (id, name, description, capacity, rating)
VALUES (2, "Bar Manolo", "Bar de tapas", 35, 8);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (1,"Concierto de Metallica", "Concierto de un grupo de trash metal...", now(), now(), 1);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (2,"Concierto", "Concierto varios grupos...", now(), now(), 2);

INSERT INTO Artist (artist_id, name, description, rating)
VALUES (1,"Metallica", "Grupo de Trash Metal", 8);

INSERT INTO Artist (artist_id, name, description, rating)
VALUES (2,"The Black Keys", "Grupo de Blues Rock", 9);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (1,1);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (2,1);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (2,2);

INSERT INTO User (id, name, email, type)
VALUES (1,"Angel", "angel@angel.com", 1);

INSERT INTO Comment (id, text, event_id, user_id)
VALUES (1,"Ha sido un conciertazo", 1, 1);

INSERT INTO Rating (id, rating, event_id, user_id)
VALUES (1, 9, 1, 1);

INSERT INTO Rating (id, rating, event_id, user_id)
VALUES (2, 3, 2, 1);