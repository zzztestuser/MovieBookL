CREATE DATABASE  IF NOT EXISTS `ci6206` CHARACTER SET = 'utf8mb4';
USE `ci6206`;

/** Table Creation **/
-- Email is only 100 characters due to sheningans with MySQL/MariaDB's max index length for InnoDB tables. 
-- Can be worked around, but need to muck around with innodb_large_prefix and innodb_file_format
-- which is probably out of scope of this assignment.

-- Also assumes InnoDB as the database engine

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` INT(5) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL UNIQUE,
  `name` varchar(255) NOT NULL,
  `password` varchar(100) NOT NULL,
  `profilePhotoPath` varchar(255) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modificationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);



DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `language` varchar(100) NOT NULL,
  `duration` int(3) DEFAULT NULL,
  `posterSmallPath` varchar(255) DEFAULT NULL,
  `posterMediumPath` varchar(255) DEFAULT NULL,
  `posterLargePath` varchar(255) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modificationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);



DROP TABLE IF EXISTS `genre`;
CREATE TABLE `genre` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `creationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modificationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `theatre`;
CREATE TABLE `theatre` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);




DROP TABLE IF EXISTS `user_friends`;
CREATE TABLE `user_friends` (
  `userID` int(5) NOT NULL,
  `friendID` int(5) NOT NULL,
  `inviteStatus` int(2) NOT NULL DEFAULT '0',
  `inviteDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `statusDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userID`,`friendID`),
  CONSTRAINT `fk_user_friends_friendID` FOREIGN KEY (`friendID`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_friends_userID` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);



DROP TABLE IF EXISTS `user_genre_likes`;
CREATE TABLE `user_genre_likes` (
  `userID` int(5) NOT NULL,
  `genreID` int(5) NOT NULL,
  `creationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userID`,`genreID`),
  CONSTRAINT `fk_user_genre_likes_genreID` FOREIGN KEY (`genreID`) REFERENCES `genre` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_genre_likes_userID` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);



DROP TABLE IF EXISTS `user_messages`;
CREATE TABLE `user_messages` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `from` int(5) NOT NULL,
  `to` int(5) NOT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modificationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`modificationDateTime`),
  UNIQUE KEY `from_to_date_unique` (`to`,`from`,`creationDateTime`),
  CONSTRAINT `fk_user_messages_from` FOREIGN KEY (`from`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_messages_to` FOREIGN KEY (`to`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) COMMENT='Stores messages between two users. Note: FK constraint on from and is not ON DELETE CASCADE as I think that retaining messages even if the user is deleted is worth it.';


DROP TABLE IF EXISTS `movie_genre`;
CREATE TABLE `movie_genre` (
  `movieID` int(5) NOT NULL,
  `genreID` int(5) NOT NULL,
  `creationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`movieID`,`genreID`),
  CONSTRAINT `fk_movie_genre_genreID` FOREIGN KEY (`genreID`) REFERENCES `genre` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_movie_genre_movieID` FOREIGN KEY (`movieID`) REFERENCES `movie` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);




DROP TABLE IF EXISTS `movie_screenings`;
CREATE TABLE `movie_screenings` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `movieID` int(5) NOT NULL,
  `theatreID` int(5) NOT NULL,
  `screeningDateTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_movie_screenings_movie_theatre_time` (`movieID`,`theatreID`),
  CONSTRAINT `fk_movie_screenings_movieID` FOREIGN KEY (`movieID`) REFERENCES `movie` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_movie_screenings_theatreID` FOREIGN KEY (`theatreID`) REFERENCES `theatre` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);



DROP TABLE IF EXISTS `movie_user_likes`;
CREATE TABLE `movie_user_likes` (
  `userID` int(5) NOT NULL,
  `movieID` int(5) NOT NULL,
  `creationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`movieID`,`userID`),
  CONSTRAINT `fk_movie_user_likes_movieID` FOREIGN KEY (`movieID`) REFERENCES `movie` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_movie_user_likes_userID` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) COMMENT='Table to allow users to like specific movies outside their default genres';




DROP TABLE IF EXISTS `movie_user_reviews`;
CREATE TABLE `movie_user_reviews` (
  `movieID` int(5) NOT NULL,
  `userID` int(5) NOT NULL,
  `comment` varchar(1000) DEFAULT NULL,
  `rating` int(2) NOT NULL,
  `creationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modificationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`movieID`,`userID`),
  CONSTRAINT `fk_movie_user_reviews_movieID` FOREIGN KEY (`movieID`) REFERENCES `movie` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_movie_user_reviews_userID` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);


DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `screeningID` int(5) DEFAULT NULL,
  `createdBy` int(5) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modificationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_event_createdBy` FOREIGN KEY (`createdBy`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_screeningID` FOREIGN KEY (`screeningID`) REFERENCES `movie_screenings` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION
);



DROP TABLE IF EXISTS `event_attendees`;
CREATE TABLE `event_attendees` (
  `eventID` int(5) NOT NULL,
  `userID` int(5) NOT NULL,
  `attendanceStatus` int(1) NOT NULL DEFAULT '0',
  `inviteDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `statusDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`eventID`,`userID`),
  CONSTRAINT `fk_event_attendees_eventID` FOREIGN KEY (`eventID`) REFERENCES `event` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_attendees_user` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);



DROP TABLE IF EXISTS `event_comments`;
CREATE TABLE `event_comments` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `eventID` int(5) DEFAULT NULL,
  `userID` int(5) DEFAULT NULL,
  `comment` varchar(1000) DEFAULT NULL,
  `creationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modificationDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_event_comments_eventID` FOREIGN KEY (`eventID`) REFERENCES `event` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_comments_userID` FOREIGN KEY (`userID`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

