package kakao.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/18
 * Time : 6:27 AM
 */

@Getter
@Entity
public class Receiver extends Member {
    @Builder
    public Receiver(Long id, int receivedMoney) {
        super(id);
        this.receivedMoney = receivedMoney;
    }

    @Column(name = "received_money")
    private int receivedMoney;
}
