-- CREATE TABLE `Employee` (
--   `id` int(11) NOT NULL AUTO_INCREMENT,
--   `age` int(11) NOT NULL,
--   `name` varchar(255) DEFAULT NULL,
--   PRIMARY KEY (`id`)
-- )

DROP TABLE Rating;
DROP TABLE Comment;
DROP TABLE User;
DROP TABLE Event_Artist;
DROP TABLE Artist;
DROP TABLE Event;
DROP TABLE Local;

CREATE TABLE Local (
	id int (11) NOT NULL AUTO_INCREMENT,
	name varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	capacity int(11) NOT NULL,
	rating float(53) NOT NULL,
	CONSTRAINT local_PK PRIMARY KEY (id)
);

CREATE TABLE Event (
	id int (11) NOT NULL AUTO_INCREMENT,
	name varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	begin_date TIMESTAMP NOT NULL,
	end_date TIMESTAMP NOT NULL,
	local_id int (11) NOT NULL,
	CONSTRAINT event_PK PRIMARY KEY (id),
	CONSTRAINT eventLocalIdFK FOREIGN KEY(local_id) REFERENCES Local (id)
);

CREATE TABLE Artist (
	artist_id int (11) NOT NULL AUTO_INCREMENT,
	name varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	rating float(53) NOT NULL,
	CONSTRAINT artist_PK PRIMARY KEY (artist_id)
);

CREATE TABLE Event_Artist
(
    event_id INT NOT NULL,  
    artist_id INT NOT NULL,  
    PRIMARY KEY (event_id, artist_id),  
    FOREIGN KEY (event_id) REFERENCES Event(id) ON UPDATE CASCADE,  
    FOREIGN KEY (artist_id) REFERENCES Artist(artist_id) ON UPDATE CASCADE
);

CREATE TABLE User (
	id int (11) NOT NULL AUTO_INCREMENT,
	name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	type int (11) NOT NULL,
	CONSTRAINT user_PK PRIMARY KEY (id)
);

CREATE TABLE Comment (
	id int (11) NOT NULL AUTO_INCREMENT,
	text varchar(255) NOT NULL,
	event_id int (11) NOT NULL,
	user_id int (11) NOT NULL,
	CONSTRAINT comment_PK PRIMARY KEY (id),
	CONSTRAINT commentEventIdFK FOREIGN KEY(event_id) REFERENCES Event (id),
	CONSTRAINT commentUserIdFK FOREIGN KEY(user_id) REFERENCES User (id)	
);

CREATE TABLE Rating (
	id int (11) NOT NULL AUTO_INCREMENT,
	rating float(53) NOT NULL,
	event_id int (11) NOT NULL,
	user_id int (11) NOT NULL,
	CONSTRAINT rating_PK PRIMARY KEY (id),
	CONSTRAINT ratingEventIdFK FOREIGN KEY(event_id) REFERENCES Event (id),
	CONSTRAINT ratingUserIdFK FOREIGN KEY(user_id) REFERENCES User (id)	
);

