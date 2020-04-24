## 결재 요청 API
결제를 요청하고 카드사에 약속된 문자열을 전송한다.

`POST /api/v1/payments`

### Body
- 필수 입력 항목
	- **cardnum** - 카드 번호 (10-16 자리 숫자)
	- **exp** - 유효기간 (4자리 숫자. mmyy)
	- **cvc** - 3자리 숫자
	- **plan** - 할부개월수 : 0(일시불), 1 - 12 
	- **amount** - 결제금액(100원 이상, 10억원 이하. 숫자)
- 선택 입력
	- **vat** - 부가가치세. 입력하지 않으면 자동으로 계산함.



### Response

- **id** - 관리번호
- **dateTime** - 거래 일시

### Example Request
`POST /api/v1/payments`

```javascript
{
  "cardnum": "1234567890123456",
  "exp": 1224,
  "amount": 1000000000,
  "cvc" : 123,
  "plan": 0,
  "vat": 10000
}
```

### Example Response
`200 OK`

```javascript
{
    "id": "381827847bc4432b85a4",
    "cardnum": "1234567890123456",
    "cvc": 123,
    "exp": 1224,
    "plan": 0,
    "amount": 1000000000,
    "dateTime": "2020-04-24T09:51:20.655",
    "vat": 10000,
    "payment": true
}
```

### Errors Example

`400 BadRequest`

```javascript
[
    {
        "field": "cardnum",
        "objectName": "paymentModel",
        "code": "WrongValue",
        "defaultMessage": "카드 번호가 너무 깁니다.",
        "rejectedValue": "1234567890123456aa"
    }
]
```

