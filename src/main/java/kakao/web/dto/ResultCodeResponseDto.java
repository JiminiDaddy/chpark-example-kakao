package kakao.web.dto;

import kakao.domain.ErrorCode;
import lombok.Getter;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/23
 * Time : 1:46 AM
 */

@Getter
public class ResultCodeResponseDto {
    private int resultCode;
    private String resultMessage;

    public ResultCodeResponseDto(ErrorCode errorCode) {
       this.resultCode = errorCode.getCode();
       this.resultMessage = errorCode.name();
    }
}
