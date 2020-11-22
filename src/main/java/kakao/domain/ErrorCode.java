package kakao.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/22
 * Time : 1:59 AM
 */

@Getter
@NoArgsConstructor
public enum ErrorCode {
    SUCCESS(100),

    WRONG_ACCESS_TOKEN(110),

    WRONG_ROOM(120),

    WRONG_SENDER(130),
    ALREADY_IS_RECEIVED_MEMBER(131),
    SENDER_CANNOT_RECEIVE_MONEY(132),
    OVER_THROW_COUNT(140),

    TIMEOUT_CHECK_BALANCE(150),
    TIMEOUT_MONEY_RECEIVE(151)
    ;

    ErrorCode(int code) {
       this.code = code;
    }

    private int code;
}
