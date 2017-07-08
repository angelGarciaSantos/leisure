-- INSERT INTO table_name (column1,column2,column3,...)
-- VALUES (value1,value2,value3,...);

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (1, "Bar Pepe", "Bar de copas", 40, 43.3380199, -8.4074581, "http://guitarchic.net/wp-content/uploads/2017/01/1-1.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (2, "Bar Manolo", "Bar de tapas", 35, 43.3380199, -8.4074581, "http://guitarchic.net/wp-content/uploads/2017/01/1-1.jpg");

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (1,"Concierto de Metallica", "Concierto de un grupo de trash metal...", now(), now(), 1);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (2,"Concierto", "Concierto varios grupos...", now(), now(), 2);

INSERT INTO Artist (artist_id, name, description, image)
VALUES (1,"Metallica", "Grupo de Trash Metal", "https://pbs.twimg.com/profile_images/766360293953802240/kt0hiSmv.jpg");

INSERT INTO Artist (artist_id, name, description, image)
VALUES (2,"The Black Keys", "Grupo de Blues Rock", "http://i.imgur.com/1asNWI9.png");

INSERT INTO Artist (artist_id, name, description, image)
VALUES (3,"The Strokes", "Grupo de Rock", "http://abcblogs.abc.es/alvaro-alonso/wp-content/uploads/sites/134/2013/07/the_strokes_2.jpg");

INSERT INTO Artist (artist_id, name, description, image)
VALUES (4,"Joaquin Reyes", "Humorista", "http://www.jotdown.es/wp-content/uploads/2012/11/Joaqu%C3%ADn-Reyes-para-Jot-Down-3.jpg");

INSERT INTO Artist (artist_id, name, description, image)
VALUES (5,"Luis Piedrahita", "Mago y humorista", "https://yalastengo.com/wp-content/uploads/2016/05/Luis-Piedrahita-740x589.jpg");

INSERT INTO Artist (artist_id, name, description, image)
VALUES (6,"Ernesto Sevilla", "Humorista", "http://revistahsm.com/wp-content/uploads/2013/10/ernesto-sevilla.jpg");

INSERT INTO Artist (artist_id, name, description, image)
VALUES (7,"Jorge Blass", "Mago", "http://www.redescena.net/temfiles/matgrafico/pq/retrato_j._blass_2mb.jpg");

INSERT INTO Tag (tag_id, name)
VALUES (1,"Rock");

INSERT INTO Tag (tag_id, name)
VALUES (2,"Blues");

INSERT INTO Tag (tag_id, name)
VALUES (3,"Jazz");

INSERT INTO Tag (tag_id, name)
VALUES (4,"Humor");

INSERT INTO Tag (tag_id, name)
VALUES (5,"Magia");

INSERT INTO Tag (tag_id, name)
VALUES (6,"Metal");

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (6,1);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (1,2);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (2,2);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (1,3);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (4,4);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (4,5);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (5,5);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (1,1);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (2,1);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (2,2);

INSERT INTO User (id, name, email, password, type)
VALUES (1,"Angel", "angel@angel.com","angel", 1);

INSERT INTO User (id, name, email, password, type)
VALUES (2,"admin", "admin@admin.com","admin", 2);

INSERT INTO Interest (id, points, tag_id, user_id)
VALUES (1, 1, 1, 1);

INSERT INTO Interest (id, points, tag_id, user_id)
VALUES (2, 2, 2, 1);

INSERT INTO Interest (id, points, tag_id, user_id)
VALUES (3, 2, 3, 1);

INSERT INTO Interest (id, points, tag_id, user_id)
VALUES (4, 1, 6, 1);

INSERT INTO User_Artist (user_id, artist_id)
VALUES (1,1);

INSERT INTO User_Event (user_id, event_id)
VALUES (1,1);

INSERT INTO Comment (id, text, date, event_id, user_id)
VALUES (1,"Ha sido un conciertazo", now(), 1, 1);

INSERT INTO Rating (id, rating, event_id, user_id)
VALUES (1, 9, 1, 1);

INSERT INTO Rating (id, rating, event_id, user_id)
VALUES (2, 3, 2, 1);