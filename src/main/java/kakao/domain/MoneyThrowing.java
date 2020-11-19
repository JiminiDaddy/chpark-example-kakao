package kakao.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.TreeSet;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/18
 * Time : 6:10 AM
 */

@Getter
@Entity
@Table(name = "MoneyThrowings")
public class MoneyThrowing {
    @Id @Column(length = 3)
    private String accessToken;

    private int moneyAmount;

    private int throwCount;

    private long senderId;

    private String roomId;

    private LocalDateTime startTime;

    @OneToMany
    private TreeSet<Receiver> receivers = new TreeSet<>();

    @Builder
    public MoneyThrowing(String accessToken, String roomId, long senderId, int moneyAmount, int throwCount) {
        this.accessToken = accessToken;
        this.roomId = roomId;
        this.senderId = senderId;
        this.moneyAmount = moneyAmount;
        this.throwCount = throwCount;
        this.startTime = LocalDateTime.now();
    }

    public int receiveMoney(Member member) {
        int money = 0;
        if (receivers.size() < throwCount - 1) {
            money = moneyAmount / throwCount;
        } else {
            money = moneyAmount - (receivers.size() + 1) * moneyAmount / throwCount;
        }
        Receiver receiver = Receiver.builder()
                .id(member.getId())
                .receivedMoney(money)
                .build();
        receivers.add(receiver);
        return receiver.getReceivedMoney();
    }
}
