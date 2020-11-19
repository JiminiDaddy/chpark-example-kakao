package kakao.web.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/18
 * Time : 6:49 AM
 */

@Builder
@Getter
public class ThrowingRequestDto {
    private int moneyAmount;
    private int throwCount;
}
