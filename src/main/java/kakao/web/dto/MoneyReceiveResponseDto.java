package kakao.web.dto;

import kakao.domain.ErrorCode;
import lombok.Getter;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/20
 * Time : 12:18 AM
 */

@Getter
public class MoneyReceiveResponseDto extends ResultCodeResponseDto{
    private int receiveMoneyAmount;

    public MoneyReceiveResponseDto(ErrorCode errorCode) {
        super(errorCode);
    }

    public MoneyReceiveResponseDto(ErrorCode errorCode, int receiveMoneyAmount) {
        super(errorCode);
        this.receiveMoneyAmount = receiveMoneyAmount;
    }
}
