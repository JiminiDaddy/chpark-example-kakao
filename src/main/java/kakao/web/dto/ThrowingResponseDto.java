package kakao.web.dto;

import kakao.domain.ErrorCode;
import lombok.Getter;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/22
 * Time : 11:05 PM
 */

@Getter
public class ThrowingResponseDto {
    private int resultCode;

    private String resultMessage;

    private String accessToken;

    public ThrowingResponseDto(ErrorCode errorCode, String accessToken) {
        this.resultCode = errorCode.getCode();
        this.resultMessage = errorCode.name();
        this.accessToken = accessToken;
    }
}
