# Settings
아래 파일에서 mysql username과 password를 세팅한다.
/mileage/src/main/resources/application.properties
```
spring.datasource.username='your username'
spring.datasource.password='your password'
```

----
# Installation
```
-> cd /'Project Directory'
-> ./gradlew build
-> cd /lib
-> java -jar 'filename'.jar
```
----
# Description
## (Get - Json) "/point/{userId}" : 포인트 조회 
- input : userId
- output : UserDTO(userId, point)

## (Get - Json) "/point/log/{userId}" : 포인트 변경 로그 조회 
- input : userId
- output : List<PointLogDTO>

## (Post - Json) "/events" : ADD, MOD, DELETE
- input : EventDTO(type, action, reviewId, content, attachedPhotoIds, userId, placeId)

- ADD, MOD
output: ReviewDTO(reviewId, placeId, userId, content, linkPhotos, isFirstAtPlace)

- DELETE
output : http.ok

----
# condition
Review ADD의 경우, 어플리케이션 과제 확인을 수월하게 하기위해 User가 없을때는, User를 추가한 뒤 다음 로직을 정상 진행.
  
Place 테이블은, 경합 상황을 고려하여 Lock을 이용해서 동시성을 제어하고, 테이블을 따로 분리.

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
