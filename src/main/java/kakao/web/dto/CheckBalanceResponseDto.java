package kakao.web.dto;

import kakao.domain.MoneyThrowing;
import kakao.domain.Receiver;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/18
 * Time : 6:23 AM
 */

@Getter
public class CheckBalanceResponseDto {
    private LocalDateTime throwingTime;
    private int throwingMoney;
    private int receivedMoney;
    private List<Receiver> receiverInfos;

    public CheckBalanceResponseDto(MoneyThrowing entity) {
        this.throwingTime = entity.getStartTime();
        this.throwingMoney = entity.getMoneyAmount();
        for (Receiver receiverInfo : receiverInfos) {
            receivedMoney += receiverInfo.getReceivedMoney();
        }
    }
}
