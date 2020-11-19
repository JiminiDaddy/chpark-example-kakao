package kakao.web.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/20
 * Time : 12:18 AM
 */

@Builder
@Getter
public class MoneyReceiveResponseDto {
    private int receiveMoneyAmount;
}
