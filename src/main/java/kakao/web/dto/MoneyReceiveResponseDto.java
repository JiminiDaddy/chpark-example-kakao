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
public class MoneyReceiveResponseDto {
    private int receiveMoneyAmount;

    private int resultCode;

    private String resultMessage;

    public MoneyReceiveResponseDto(ErrorCode errorCode) {
        this.resultCode = errorCode.getCode();
        this.resultMessage = errorCode.name();
    }

    public MoneyReceiveResponseDto(ErrorCode errorCode, int receiveMoneyAmount) {
        this.resultCode = errorCode.getCode();
        this.resultMessage = errorCode.name();
        this.receiveMoneyAmount = receiveMoneyAmount;
    }
}
