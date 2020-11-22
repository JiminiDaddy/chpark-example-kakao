package kakao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/18
 * Time : 6:10 AM
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "MoneyThrowings")
public class MoneyThrowing {
    @Id @Column(length = 3)
    private String accessToken;

    private int moneyAmount;

    @Transient
    private int receivedMoneyAmount;

    @Transient
    private ErrorCode errorCode = ErrorCode.SUCCESS;

    private int throwCount;

    private Long senderId;

    private String roomId;

    private LocalDateTime startTime;

    @ElementCollection
    private Map<Long, Integer> receivers = new HashMap<>();

    @Builder
    public MoneyThrowing(String accessToken, String roomId, long senderId, int moneyAmount, int throwCount) {
        this.accessToken = accessToken;
        this.roomId = roomId;
        this.senderId = senderId;
        this.moneyAmount = moneyAmount;
        this.throwCount = throwCount;
        this.startTime = LocalDateTime.now();
    }

    public int receiveMoney(String roomId, Long memberId) {
        if (!isValidRoom(roomId)) {
            this.errorCode = ErrorCode.WRONG_ROOM;
        }
        else if (isSender(memberId)) {
            this.errorCode = ErrorCode.SENDER_CANNOT_RECEIVE_MONEY;
        }
        else if (isAlreadyReceivedMember(memberId)) {
            this.errorCode = ErrorCode.ALREADY_IS_RECEIVED_MEMBER;
        }
        else if (isTimeOverReceiveMoney()) {
            this.errorCode = ErrorCode.TIMEOUT_MONEY_RECEIVE;
        }
        else if (isOverThrowCount()) {
            this.errorCode = ErrorCode.OVER_THROW_COUNT;
        }

        if (this.errorCode != ErrorCode.SUCCESS) {
            return 0;
        }

        // 전체금액 / 인원수로 공평하게 돈을 뿌리되 마지막 멤버는 남은 값을 다 가져간다. (나머지 값이 소수점인경우 그냥 나누면 잔고가 남을 수 있다.)
        int money;
        if (receivers.size() < throwCount - 1) {
            money = moneyAmount / throwCount;
        } else {
            money = moneyAmount - (receivers.size() * moneyAmount) / throwCount;
        }

        receivers.put(memberId, money);
        return money;
    }

    public void checkBalance(String roomId, Long memberId) {
        if (!isValidRoom(roomId)) {
            this.errorCode = ErrorCode.WRONG_ROOM;
        }
        else if (!isSender(memberId)) {
            this.errorCode = ErrorCode.WRONG_SENDER;
        }
        else if (isTimeOverCheckBalance()) {
            this.errorCode = ErrorCode.TIMEOUT_CHECK_BALANCE;
        }

        if (this.errorCode != ErrorCode.SUCCESS) {
           return;
        }

        receivedMoneyAmount = 0;
        Set<Long> receiverIds = receivers.keySet();
        for (Long receiverId : receiverIds) {
            receivedMoneyAmount += receivers.get(receiverId);
        }
    }

    private boolean isValidRoom(String roomId) {
        return this.roomId.equals(roomId);
    }

    private boolean isSender(Long senderId) {
        return this.senderId.equals(senderId);
    }

    private boolean isTimeOverCheckBalance() {
        return LocalDateTime.now().isAfter(startTime.plusDays(7));
    }

    private boolean isTimeOverReceiveMoney() {
        return LocalDateTime.now().isAfter(startTime.plusMinutes(10));
    }

    private boolean isAlreadyReceivedMember(Long memberId) {
        return receivers.containsKey(memberId);
    }

    private boolean isOverThrowCount() {
       return throwCount <= receivers.size();
    }
}
