## KakaoPay 뿌리기 기능 구현

## 문제해결 전략
### 기능 최소화
~~개발 첫날 채팅방, 사용자 관리까지 함께 구현을 진행하려고 했으나 프로그램이 복잡해지고 요구사항에 채팅방과 사용자 관리에 대해선 언급이 없기때문에 제거함~~  
뿌리기 Entity에서 채팅방과 사용자 Entity와의 연관 관계를 없애고 Key(String, Long)을 저장하는 방식으로 구현

```java
// before
public Class MoneyThrowing {
  ...
  private Room room;
  private Member sender;
  ...
}
// after
public Class MoneyThrowing {
  ...
  private String roomId;
  private Long senderId;
  ...
}
```

AccessToken은 중복되지 않는 임의의 3자리 문자열이므로 가장 간단한 방법으로 UUID를 통해 3자리를 만들고, 만든 Token이 기존에 저장된 값이 포함되지 않을때까지 다시 생성하는 방식으로 진행

<hr/>

### 설계
뿌리기 도메인에서 받기/조회하기 기능을 수행할 때, 유효 체크(Token, Room, Member, Time 등등)에 따라 에러코드를 생성함으로써 성공/실패 여부를 서비스 영역으로 전가하지 않도록 구현함

AccessToken 유효체크는 비즈니스 로직이므로 서비스 영역에서 처리함으로써, 표현 영역에서는 서비스에서 발생한 예외에 따른 예외처리만 진행하도록 구현함

RoomId와 UserId의 유효성 체크(Null check)는 표현 영역에서 처리함으로써, 잘못된 값이 비즈니스 로직으로 넘어가지 않도록 구현함


<hr/>

### 테스트
Junit5를 사용했고, 단위 테스트로는 Controller API Test를 진행했고 통합 테스트로는 각 기능 및 제약사항에 대해 진행함

통합 테스트 후에는 Postman으로 각 기능별 응답코드를 확인하면서 기능 테스트를 진행함


