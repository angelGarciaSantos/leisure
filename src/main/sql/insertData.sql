-- INSERT INTO table_name (column1,column2,column3,...)
-- VALUES (value1,value2,value3,...);



INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (1, "Playa Club", "Sonidos eclécticos de vanguardia y música en directo en una discoteca y sala de conciertos abierta en 1957.", 500, 43.36853944004338, -8.413666470552245, "https://alvaropardoinfo4.files.wordpress.com/2012/05/playaclub.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (2, "Sala Mardigras", "Sala de conciertos.", 300, 43.37464111624276, -8.398218548479454, "http://empuje.net/wp-content/uploads/2017/03/mardi17.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (3, "Filomatic", "Sala de conciertos.", 250, 43.37442606541591, -8.395808412268488, "https://www.paxinasgalegas.es/imagenes/sala-filomatic_img551740t0.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (4, "Garufa Club", "Sala de conciertos.", 250, 43.3675283, -8.4111167, "http://ocio.laopinioncoruna.es/img_contenido/agenda-lugar/2014/07/7367/55083009__275x250.png");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (5, "Pelicano", "Sala de conciertos.", 250, 43.3680924, -8.4003027, "https://i2.wp.com/salapelicano.com/wp-content/uploads/2015/06/sala-pelicano-inauguracion.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (6, "Jazz Filloa", "Sala de conciertos.", 250, 43.3714525, -8.4031857, "http://vivirnacoruna.es/wp-content/uploads/2014/04/jazz-filloa-21.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (7, "El Pantalan", "Sala de conciertos.", 250, 43.3480962, -8.3874647, "http://photos1.blogger.com/x/blogger2/4801/3855/1600/650567/164-6406_IMG.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (8, "Baba Bar", "Sala de conciertos.", 250, 43.3653743, -8.415454, "http://static.guiaocio.com/var/guiadelocio.com/storage/images/tarde-y-noche/a-coruna/a-coruna/baba-bar/galeria/baba-bar-a-coruna/15060888-1-esl-ES/baba-bar-a-coruna.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (9, "Coliseum", "Sala de conciertos y espectáculos.", 11000 , 43.33855, -8.4116757, "http://www.coruna.gal/IMG/P_Entidad_1442849361293_800_800_U_a94467447b65d6aecd25b7a6771bbf8.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (10, "Teatro Colón", "Moderno teatro con ornamentada sala que organiza obras de teatro, conciertos y espectáculos de comedia.", 1000 , 43.3689539, -8.4026657, "http://www.coruna.gal/IMG/P_Entidad_1442851270170_800_800_U_d6c02452cd1744f662379f46492c9c.jpg");

INSERT INTO Local (id, name, description, capacity, lat, lng, image)
VALUES (11, "Teatro Rosalía de Castro", "Proyecciones cinematográficas, teatro y espectáculos de música en clásica sala de teatro de 1841.", 1000 , 43.3702446, -8.4006854, "http://www.coruna.gal/IMG/P_Entidad_1442850510163_800_800_U_c3e6f2464c17e4035943e2734cf061.jpg");


INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (1,"Metallica en concierto", "Concierto del conocido grupo de trash metal.", TIMESTAMP '2017-10-12 22:15:31.123456789', TIMESTAMP '2017-10-12 00:15:31.123456789', 9);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (2,"Concierto", "Concierto varios grupos...", now(), now(), 2);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (3,"Festival de verano", "Varios conocidos grupos actuarán en la primera edición de este festial.", TIMESTAMP '2017-10-14 19:00:00.000000000', TIMESTAMP '2017-10-16 15:00:00.000000000', 9);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (4,"Joaquín Reyes en vivo", "Actuación del conocido cómico que promete arrancarnos unas carcajadas.", TIMESTAMP '2017-10-20 22:00:00.000000000', TIMESTAMP '2017-10-20 23:00:00.000000000', 10);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (5,"Festival de cómicos", "Varios populares humoristas se reunirán para el disfrute de todos.", TIMESTAMP '2017-10-22 19:00:00.000000000', TIMESTAMP '2017-10-22 23:45:00.000000000', 10);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (6,"Concierto de Vintage Trouble", "El inigualable grupo de Los Angeles tocará en nuestra ciudad.", TIMESTAMP '2017-10-23 21:00:00.000000000', TIMESTAMP '2017-10-23 23:55:00.000000000', 5);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (7,"Concierto de The Strokes", "La formación estadounidense se dejará ver en un unico concierto en nuestro país.", TIMESTAMP '2017-10-23 22:00:00.000000000', TIMESTAMP '2017-10-23 23:55:00.000000000', 2);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (8,"Concierto de Queens of the Stone Age", "El famoso grupo presentará su nuevo disco.", TIMESTAMP '2017-10-25 21:00:00.000000000', TIMESTAMP '2017-10-23 23:55:00.000000000', 5);

INSERT INTO Event (id, name, description, begin_date, end_date, local_id)
VALUES (9,"Concierto de Rammstein", "Los alemanes se darán cita en nuestra ciudad.", TIMESTAMP '2017-10-28 21:00:00.000000000', TIMESTAMP '2017-10-28 23:55:00.000000000', 9);

INSERT INTO Artist (artist_id, name, description, image)
VALUES (1,"Metallica", "Metallica es una banda de heavy metal estadounidense originaria de Los Angeles, pero con base en San Francisco desde febrero de 1983. Fue fundada en 1981 en Los Angeles por Lars Ulrich y James Hetfield, a los que se les unirían Dave Mustaine y Cliff Burton. Estos dos músicos fueron después sustituidos por el guitarrista Kirk Hammett y el bajista Jason Newsted, Dave Mustaine fue despedido un ano después de ingresar en la banda debido a su excesiva adicción al alcohol y su actitud violenta, y fundó la banda Megadeth, siendo sustituido por Kirk Hammett ex guitarrista de Exodus. Por otra parte, el 27 de septiembre de 1986, la muerte de Cliff Burton en un accidente de autobús en Suecia, durante una de sus giras, provoco la entrada al grupo de Jason Newsted, quien, tras su abandono quince anos mas tarde, seria sustituido por el bajista actual, Robert Trujillo.", "https://pbs.twimg.com/profile_images/766360293953802240/kt0hiSmv.jpg");

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

INSERT INTO Artist (artist_id, name, description, image)
VALUES (8,"Queens Of The Stone Age", "Queens of the Stone Age (también conocidos como QOTSA o, simplemente, Queens) es una banda estadounidense de stoner rock originaria de Palm Desert, California. Fue formada en 1997 por Josh Homme, dos años después de la desintegración de Kyuss, su banda anterior, y contó con la participación de sus excompañeros Nick Oliveri y Alfredo Hernández.", "http://rockaxis.com/sites/default/files/node/novedades/imagen/128476_0.jpg");

INSERT INTO Artist (artist_id, name, description, image)
VALUES (9,"Royal Blood", "Royal Blood es un dúo británico de rock formado en Worthing, Inglaterra en el 2013. El sonido de la banda se centra especialmente en una mezcla de Garage Rock, Metal Alternativo y Blues Rock, y su principal caracterización es su puesta en primer plano de un bajo y la no inclusión de guitarras. Los miembros que conforman el dúo son el bajista Mike Kerr y el baterista Ben Thatcher.", "https://www.ruta66.es/wp-content/uploads/2017/07/royalblood.jpg");

INSERT INTO Artist (artist_id, name, description, image)
VALUES (10,"Vintage Trouble", "Vintage Trouble es una banda estadounidense de blues rock originaria de Los Ángeles, California, formada en 2010. Está integrada por Ty Taylor (voz), Nalle Colt (guitarra), Rick Barrio Dill (bajo) y Richard Danielson (batería). El sonido de la banda ha sido descrito como una mezcla entre blues, soul y rock and roll, principales géneros musicales de los años cincuenta y sesenta, con el mismo Ty Taylor agregando 'imagina a James Brown cantando para Led Zeppelin y te harás una idea de nuestro sonido'", "http://www.vintagetrouble.com/wp-content/themes/thestory-child/images/m-hero.png");

INSERT INTO Artist (artist_id, name, description, image)
VALUES (11,"Mastodon", "Mastodon es una banda estadounidense de metal progresivo fundada el 13 de enero del 2000 por los guitarristas Brent Hinds y Bill Kelliher, el baterista Brann Dailor y el bajista Troy Sanders en Atlanta. Aunque comenzaron como quinteto, con el vocalista Eric Saner, la banda se caracteriza por ser un cuarteto en el que los miembros comparten las labores vocales en los distintos temas. Con la excepción de su álbum debut, Remission, todos los trabajos de la banda han entrado en el Billboard 200; a destacar los top 10 alcanzados por The Hunter y Once More 'Round the Sun.", "http://musiconworldoffmx.com/wp-content/uploads/2016/05/Mastodon.jpg");

INSERT INTO Artist (artist_id, name, description, image)
VALUES (12,"Rammstein", "Rammstein es una banda alemana de metal industrial formada en 1994 por los músicos Till Lindemann, Richard Z. Kruspe, Oliver Riedel, Paul Landers, Christian Lorenz y Christoph Schneider. Su música se basa en una corriente surgida en su país en los años 1990 llamada Neue Deutsche Härte del que son su exponente más popular y al que también pertenecen, entre otros, Oomph! y Die Krupps. Ellos mismos han denominado en alguna ocasión esta mezcla con el apelativo de Tanzmetall (metal de baile).", "http://www.sopitas.com/wp-content/uploads/2015/12/rammstein.jpeg");


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

INSERT INTO Tag (tag_id, name)
VALUES (7,"Stoner");

INSERT INTO Tag (tag_id, name)
VALUES (8,"Infantil");

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
VALUES (4,6);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (5,7);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (7,8);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (5,5);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (1,9);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (1,10);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (2,10);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (6,11);

INSERT INTO Tag_Artist (tag_id, artist_id)
VALUES (6,12);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (1,1);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (2,1);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (2,2);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (3,2);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (3,3);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (3,8);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (3,9);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (3,11);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (4,4);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (5,4);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (5,5);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (5,6);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (6,10);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (7,3);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (8,8);

INSERT INTO Event_Artist (event_id, artist_id)
VALUES (9,12);

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

INSERT INTO Rating (id, rating, date, event_id, user_id)
VALUES (1, 9, now(), 1, 1);

INSERT INTO Rating (id, rating, date, event_id, user_id)
VALUES (2, 3, now(), 2, 1);