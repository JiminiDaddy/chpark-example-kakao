package kakao.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.domain.ErrorCode;
import kakao.service.MoneyThrowingService;
import kakao.web.dto.MoneyReceiveRequestDto;
import kakao.web.dto.MoneyReceiveResponseDto;
import kakao.web.dto.ThrowingRequestDto;
import kakao.web.dto.ThrowingResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/19
 * Time : 11:36 PM
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class MoneyThrowingControllerTest {
    private static final String SERVER_URL = "http://localhost:8080/api/v1/money-throw";
    private long senderId = 11111;
    private String roomId = "room_01";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MoneyThrowingService moneyThrowingService;

    @Test
    void 뿌리기() throws Exception {
        int throwCount = 5;
        int moneyAmount = 1000;
        ThrowingRequestDto requestDto = ThrowingRequestDto.builder().throwCount(throwCount).moneyAmount(moneyAmount).build();
        ThrowingResponseDto responseDto = new ThrowingResponseDto(ErrorCode.SUCCESS, "AAA");

        given(moneyThrowingService.throwMoney(requestDto, roomId, senderId)).willReturn(responseDto);
        mvc.perform(post(SERVER_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto))
                .header("X-USER-ID", senderId)
                .header("X-ROOM-ID", roomId)
        ).andExpect(status().isOk());
    }

    @Test
    void 조회하기() throws Exception {
        String accessToken = "abc";
        mvc.perform(get(SERVER_URL + "/" + accessToken)
                .header("X-USER-ID", senderId)
                .header("X-ROOM-ID", roomId)
        ).andExpect(status().isOk());
    }

    @Test
    void 받기() throws Exception {
        String accessToken = "abc";
        MoneyReceiveRequestDto requestDto = MoneyReceiveRequestDto.builder().accessToken(accessToken).build();
        MoneyReceiveResponseDto responseDto = new MoneyReceiveResponseDto(ErrorCode.SUCCESS, 500);
        given(moneyThrowingService.receiveMoney(requestDto)).willReturn(responseDto);

        mvc.perform(put(SERVER_URL + "/" + accessToken)
                .header("X-USER-ID", senderId)
                .header("X-ROOM-ID", roomId)
        ).andExpect(status().isOk()).andDo(print());
    }
}
