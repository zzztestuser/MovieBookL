USE `ci6206`;

DELIMITER ;;
CREATE TRIGGER `trg_user_before_update` BEFORE UPDATE ON `user` FOR EACH ROW
BEGIN
	SET NEW.modificationDateTime = NOW();
END ;;


CREATE TRIGGER `trg_movie_before_update` BEFORE UPDATE ON `movie` FOR EACH ROW
BEGIN
	SET NEW.modificationDateTime = NOW();
END ;;



CREATE TRIGGER `user_messages_BEFORE_UPDATE` BEFORE UPDATE ON `user_messages` FOR EACH ROW
BEGIN
	SET NEW.modificationDateTime = NOW();
END ;;


CREATE TRIGGER `movie_user_reviews_BEFORE_UPDATE` BEFORE UPDATE ON `movie_user_reviews` FOR EACH ROW
BEGIN
	SET NEW.modificationDateTime = NOW();
END;;


CREATE TRIGGER `event_comments_BEFORE_UPDATE` BEFORE UPDATE ON `event_comments` FOR EACH ROW
BEGIN
	SET NEW.modificationDateTime = NOW();
END;;
DELIMITER ;
