# Installation
```
cd /'Project Directory'/lib
java -jar 'filename'.jar
```
----
# Description
## (Post - Json) "/point" : 포인트 조회 
- input : userId
- output : UserDTO(userId, point)

- service logic
1. 클라이언트로부터 받은, userId로 User Table Select
2. user가 존재하지 않는다면, new user Insert
3. user로부터 point가 담긴 userDTO 생성 후, 반환

## (Post - Json) "/events" : 리뷰 생성(ADD), 수정(MOD), 삭제(DELETE) 
- input : EventDTO(type, action, reviewId, content, attachedPhotoIds, userId, placeId)
- output : ReviewDTO(reviewId, placeId, userId, content, linkPhotos, isFirstAtPlace)

- service logic
1.
2.
3.
4.
5.
6.
7.
----
# ERD
![TRIPLE_DB](https://user-images.githubusercontent.com/35343777/176214241-bdcc5b40-b2a0-42e1-ad06-37af9a6ff794.png)
-------
# DDL

### User
CREATE TABLE `user` (

  `user_id` varchar(255) NOT NULL,
  
  `point` bigint NOT NULL,
  
  PRIMARY KEY (`user_id`)
  
) 

### Review
CREATE TABLE `review` (

  `review_id` varchar(255) NOT NULL,
  
  `content` varchar(255) NOT NULL,

  `is_first_at_place` bit(1) NOT NULL,
  
  `place_id` varchar(255) NOT NULL,
  
  `user_id` varchar(255) NOT NULL,
  
  PRIMARY KEY (`review_id`),
  
  KEY `reviewIdx` (`place_id`,`user_id`)
  
) 

### PointLog
CREATE TABLE `point_log` (

  `log_id` bigint NOT NULL AUTO_INCREMENT,
  
  `action` varchar(255) NOT NULL,
  
  `amount` bigint NOT NULL,
  
  `review_id` varchar(255) NOT NULL,
  
  `user_id` varchar(255) DEFAULT NULL,
  
  PRIMARY KEY (`log_id`),
  
  KEY `pointLogIdx` (`user_id`),
  
  CONSTRAINT `FKiwxuemytacp89p9qppgaj84y6` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
  
)

### Place
CREATE TABLE `place` (

  `place_id` varchar(255) NOT NULL,
  
  `review_cnt` bigint NOT NULL,
  
  PRIMARY KEY (`place_id`)
  
)

### Link_Photo
CREATE TABLE `link_photo` (

  `link_id` bigint NOT NULL AUTO_INCREMENT,
  
  `photo_id` varchar(255) NOT NULL,
  
  `review_id` varchar(255) DEFAULT NULL,
  
  PRIMARY KEY (`link_id`),
  
  KEY `FK522o5rietn8cqh6sr1rbwrfc7` (`review_id`),
  
  CONSTRAINT `FK522o5rietn8cqh6sr1rbwrfc7` FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`)
  
)
