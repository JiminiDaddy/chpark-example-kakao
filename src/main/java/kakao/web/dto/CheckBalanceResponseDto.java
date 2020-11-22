package kakao.web.dto;

import kakao.domain.ErrorCode;
import kakao.domain.MoneyThrowing;
import kakao.domain.Receiver;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/18
 * Time : 6:23 AM
 */

@Getter
public class CheckBalanceResponseDto extends ResultCodeResponseDto {
    private LocalDateTime throwingTime;
    private int throwingMoneyAmount;
    private int receivedMoneyAmount;
    private List<Receiver> receiverInfos;

    public CheckBalanceResponseDto(MoneyThrowing entity) {
        super(entity.getErrorCode());

        if (entity.getErrorCode() == ErrorCode.SUCCESS) {
            this.receiverInfos = new ArrayList<>();
            this.throwingTime = entity.getStartTime();
            this.throwingMoneyAmount = entity.getMoneyAmount();
            this.receivedMoneyAmount = entity.getReceivedMoneyAmount();
            for (Map.Entry<Long, Integer> receiver : entity.getReceivers().entrySet()) {
                Receiver receiverInfo = Receiver.builder()
                        .id(receiver.getKey())
                        .receivedMoney(receiver.getValue())
                        .build();
                receiverInfos.add(receiverInfo);
            }
        }
    }

    public CheckBalanceResponseDto(ErrorCode errorCode) {
        super(errorCode);
    }
}
