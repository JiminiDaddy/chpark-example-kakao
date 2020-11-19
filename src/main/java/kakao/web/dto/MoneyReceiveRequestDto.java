package kakao.web.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/20
 * Time : 12:16 AM
 */

@Builder
@Getter
public class MoneyReceiveRequestDto {
    private String accessToken;

    private String roomId;

    private long receiverId;
}
