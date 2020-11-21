package kakao.domain;

import lombok.Getter;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/22
 * Time : 1:59 AM
 */

@Getter
public enum ErrorCode {
    SUCCESS(100),
    WRONG_ACCESS_TOKEN(110),
    WRONG_ROOM(120),
    WRONG_SENDER(130),
    ALREADY_IS_RECEIVED_MEMBER(131),
    TIMEOVER_CHECK_BALANCE(140),
    TIMEOVER_MONEY_RECEIVE(141);

    ErrorCode(int code) {
       this.code = code;
    }

    private int code;
}
