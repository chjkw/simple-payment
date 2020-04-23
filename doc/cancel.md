## 결재 취소 API

결제를 요청하고 카드사에 약속된 문자열을 전송한다.

`POST /api/v1/payments/cancel`

### Body

- 필수 입력 항목
  - **paymentId** - 결제 ID
  - **amount** - 결제금액(100원 이상, 10억원 이하. 숫자)
- 선택 입력
  - **vat** - 부가가치세. 입력하지 않으면 자동으로 계산함.

### Response

- **id** - 관리번호
- **dateTime** - 거래 일시

### Example Request

`POST /api/v1/payments/cancel`

```javascript
{
    "paymentId": "de675c5cb7984e29a448",
    "amount": 1000000000
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

```javascript
[
    {
        "field": "amount",
        "objectName": "cancelEntity",
        "code": "WrongValue",
        "defaultMessage": "입력 값이 잘못되었습니다.",
        "rejectedValue": "1000000000"
    }
]
```

