-- INSERT INTO table_name (column1,column2,column3,...)
-- VALUES (value1,value2,value3,...);

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (1, "Playa Club", "Sonidos eclécticos de vanguardia y música en directo en una discoteca y sala de conciertos abierta en 1957.", 500, 43.36853944004338, -8.413666470552245, "https://alvaropardoinfo4.files.wordpress.com/2012/05/playaclub.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (2, "Sala Mardigras", "Sala de conciertos.", 300, 43.37464111624276, -8.398218548479454, "http://empuje.net/wp-content/uploads/2017/03/mardi17.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (3, "Filomatic", "Sala de conciertos.", 250, 43.37442606541591, -8.395808412268488, "https://www.paxinasgalegas.es/imagenes/sala-filomatic_img551740t0.jpg");

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (1,"Concierto de Metallica", "Concierto de un grupo de trash metal...", now(), now(), 1);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (2,"Concierto", "Concierto varios grupos...", now(), now(), 2);

INSERT INTO Artist (artist_id, name, description, image)
VALUES (1,"Metallica", "Metallica es una banda de heavy metal estadounidense originaria de Los Ángeles, pero con base en San Francisco desde febrero de 1983. Fue fundada en 1981 en Los Ángeles por Lars Ulrich y James Hetfield, a los que se les unirían Dave Mustaine y Cliff Burton. Estos dos músicos fueron después sustituidos por el guitarrista Kirk Hammett y el bajista Jason Newsted, Dave Mustaine fue despedido un año después de ingresar en la banda debido a su excesiva adicción al alcohol y su actitud violenta, y fundó la banda Megadeth, siendo sustituido por Kirk Hammett ex guitarrista de Exodus. Por otra parte, el 27 de septiembre de 1986, la muerte de Cliff Burton en un accidente de autobús en Suecia, durante una de sus giras, provocó la entrada al grupo de Jason Newsted,1 quien, tras su abandono quince años más tarde, sería sustituido por el bajista actual, Robert Trujillo.", "https://pbs.twimg.com/profile_images/766360293953802240/kt0hiSmv.jpg");

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
VALUES (1,"Angel", "angel@angel.com","HWFw.vdjwVX0A", 1);

INSERT INTO User (id, name, email, password, type)
VALUES (2,"admin", "admin@admin.com","YU8rVoZ1TM.u2", 2);

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