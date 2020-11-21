package kakao.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/18
 * Time : 6:27 AM
 */

@Getter
@Entity
public class Receiver {
    @Builder
    public Receiver(Long id, int receivedMoney) {
        this.id = id;
        this.receivedMoney = receivedMoney;
    }

    @Id
    private Long id;

    @Column(name = "received_money")
    private int receivedMoney;
}
