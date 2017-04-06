USE `ci6206`;
CREATE  OR REPLACE VIEW `vw_user_recommended_movies` AS

SELECT `ug`.`userID`, `m`.`id` AS `movieID` 
	FROM `user_genre_likes` `ug`, `movie` `m`, `movie_genre` `mg` 
    WHERE `m`.`id` = `mg`.`movieID` AND `ug`.`genreID` = `mg`.`genreID` 
    GROUP BY `userID`, `id`

UNION

SELECT `userID`, `movieID` 
	FROM  `movie_user_likes`
    
ORDER BY `userID`,`movieID`;
