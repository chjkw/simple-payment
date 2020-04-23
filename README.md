# simple-payment [![Build Status](https://travis-ci.com/chjkw/simple-payment.svg?token=ffY7DGiYt3uxeAUJ31eT&branch=master)](https://travis-ci.com/chjkw/simple-payment)

- 결제, 취소 요청을 받아서 string 데이터로 카드사와 통신하는 서비스

- Restful API 제공



## Tech Stack

- **Language** : Java 1.8
- **Framework** : Springboot, Lombok
- **Unit Test** : JUnit, MockMVC
- **DB** : H2, JPA



## API List

| Method | Url                     | 설명            |
| ------ | ----------------------- | --------------- |
| POST   | /api/v1/payments        | [결재 API](doc/payments.md) |
| POST   | /api/v1/payments/cancel | [결재취소 API](doc/cancel.md) |
| GET    | /api/v1/payments        | [데이터 조회 API](doc/detail.md) |



## 요구사항

- 결제 API를 제공한다. API 정의는 상단 API 목록 참조
- 결제 취소 API를 제공한다. API 정의는 상단 API 목록 참조
- 데이터 조회 API를 제공한다. API 정의는 상단 API 목록 참조
- 부분 취소 API 제공. 



## 제약사항

- 카드사 통신은 Embedded DB에 string 데이터 저장으로 대체
- 단위테스트로 각 기능 검증
- json format
- README.md 작성
- charset : UTF-8



## 문제 해결 전략

- 결제 요청 / 취소 요청은 모두 DB에 값을 추가 하므로 POST로 결정
- 입력 받은 ID의 결제 / 취소 구분을 위한 테이블 (TYPES) 추가
  - 결제 요청 : 결제 정보 생성 - 저장 - TYPES에 (id, true) 저장
  - 결제 취소 : 취소 정보 생성 - 저장 - TYPES에 (id, false) 저장
  - 결제 요청 : TYPES에서 id 조회 - true/false에 따라 결제 요청 테이블 또는 취소 테이블 조회

- 관리번호는 UUID를 조절하여 20자 고유값 생성
- 서비스를 위한 테이블 3개를 만들고, 카드사 전송 문자열 저장을 위한 테이블 1개 생성
- 카드사 전달 string 데이터는 String.format 활용
- API 호출 검증은 MockMVC 사용
- 부분 취소를 위해 취소 요청 테이블에 취소 후 남은 결제 금액, 부가가치세를 두어 DB 조회수를 최소화 함. 



## 단위 테스트 목록

- 결제 관련

  - 결제 성공
  - 결제 실패
  - 결제 취소 성공
  - 결제 취소 실패
  - 결제 정보 조회
  - 결제 취소 정보 조회
  - 부분 취소 시나리오 1
  - 부분 취소 시나리오 2
  - 부분 취소 시나리오 3

- 암호화 관련

  - 문자열 암호화 테스트

- 카드사 전달 문자열

  - 결제 정보 문자열 생성 테스트
  - 취소 정보 문자열 생성 테스트

  

## DB Schema

#### TYPES  (결제/취소 구분)

| 칼럼       | 타입        | 설명             |
| ---------- | ----------- | ---------------- |
| ID         | BIGINT      | PK               |
| IS_PAYMENT | BOOLEAN     | 결제/취소 여부   |
| UID        | VARCHAR(20) | 결제/취소 아이디 |

#### PAYMENTS (결제 요청)

| 칼럼      | 타입         | 설명               |
| --------- | ------------ | ------------------ |
| ID        | VARCHAR(20)  | 거래 ID. PK        |
| AMOUNT    | BIGINT       | 결제 금액          |
| CARDINFO  | VARCHAR(255) | 암호화된 카드 정보 |
| DATE_TIME | TIMESTAMP    | 결제 일시          |
| VAT       | BIGINT       | 부가가치세         |
| PLAN      | SMALLINT     | 할부 개월 수       |


#### CANCELS (취소 요청)
| 칼럼          | 타입        | 설명                    |
| ------------- | ----------- | ----------------------- |
| ID            | BIGINT      | 거래 ID. PK             |
| PAYMENT_ID    | VARCHAR(20) | 결제/취소 아이디        |
| AMOUNT        | BIGINT      | 취소 요청 금액          |
| VAT           | BIGINT      | 취소 요청 부가가치세    |
| REMAIN_AMOUNT | BIGINT      | 거래 후 남는 결제 금액  |
| REMAIN_VAT    | BIGINT      | 거래 후 남는 부가가치세 |

#### APPROVALS (카드사 전달 문자열)
| 칼럼     | 타입         | 설명               |
| -------- | ------------ | ------------------ |
| ID       | BIGINT       | PK                 |
| APPROVAL | VARCHAR(450) | 카드사 전달 문자열 |




## Build
~~~
mvn package
~~~

## 실행 방법
~~~
java -cp target -jar target/payment-01.jar
~~~