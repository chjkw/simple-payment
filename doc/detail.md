##  데이터 조회 API

결제를 요청하고 카드사에 약속된 문자열을 전송한다.

`GET /api/v1/payments/{id}`

### Parameter

- 필수 입력 항목
  - **id** - 결제/취소 ID

### Response

- **id** - 관리번호
- **cardnum** - 카드 번호
- **exp** - 유효기간
- **cvc** - cvc
- **plan** - 할부기간
- **amount** - 결제 취소 금액
- **vat** - 부가가치세
- **dateTime** - 거래 일시
- **payment** - 결제 취소 구분

### Example Request

`POST /api/v1/payments/cancel`

```javascript
{
    "id": "de675c5cb7984e29a448",
    "cardnum": "1234567******456",
    "cvc": 123,
    "exp": 1224,
    "plan": 0,
    "amount": 1000000000,
    "vat": 10000,
    "dateTime": "2020-04-23T23:54:20.501",
    "payment": false
}
```

### Example Response

`200 OK`

```javascript
{
    "id": "185c17b8baed4c279bcb",
    "dateTime": "2020-04-23T23:55:01",
    "remainAmount": 0,
    "remainVat": 0
}
```

### Errors Example
`400 BadRequest`
