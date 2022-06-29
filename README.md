# Settings
아래 파일에서 mysql username과 password를 세팅한다. (mysql version 8.0.29 사용)
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
![image](https://user-images.githubusercontent.com/35343777/176458415-90dd640f-5979-4171-bc40-111d1e79d5b9.png)

## ADD
1. Review Entity 생성
2. userId로 User Entity 조회
3. place에 userId로 등록된 review 존재 여부 확인(Lock획득). 존재한다면, throw AlreadyWrittenException
4. place에 review 갯수가 0개라면 user에게 부여할 포인트 1 증가
5. place cntReview 1 증가 후 update(Lock 해제)
6. photo, contents 여부에 따라 각각 포인트 1점씩 부여
7. user UPDATE
8. review INSERT
9. 저장된 review를 DTO로 변환 후 return

## MOD
1. userId로 User Entity 조회. 존재하지 않으면 throw NoDataException
2. reviewId로 Review Entity 조회. 존재하지 않으면 throw NoDataException
3. photo, contents 변동 사항에 따른 점수 변경
4. 기존의 List<LinkPhoto>를 변경된 List<LinkPhoto>로 대체
5. review UPDATE
6. 포인트 변동이 있을떄, user point 계산 -> 포인트 변경, user의 pointLog 추가 -> user UPDATE
7. update된 review를 DTO로 변환 후 return

## DELETE
1. userId로 User Entity 조회. 존재하지 않으면 throw NoDataException
2. reviewId로 Review Entity 조회. 존재하지 않으면 throw NoDataException
3. placeId로 Place Entity 조회. cntReview 1 차감 후, UPDATE
4. isFirstAtPlace, photo, contents 여부에 따라 각각 포인트 1점씩 차감
5. 점수 변동이 있다면, user의 pointLog 추가, user point 계산 후 user UPDATE
6. review 삭제

----
# condition
- Review ADD의 경우, 어플리케이션 과제 확인을 수월하게 하기위해 User가 없을때는, User를 추가한 뒤 다음 로직을 정상 진행.
  
- Place 테이블은, 경합 상황을 고려하여 Lock을 이용해서 동시성을 제어하고, 테이블을 따로 분리.

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
