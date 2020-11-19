package kakao.web.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/19
 * Time : 11:59 PM
 */

@Builder
@Getter
public class CheckBalanceRequestDto {
    private String accessToken;
    private String roomId;
    private long senderId;
}
