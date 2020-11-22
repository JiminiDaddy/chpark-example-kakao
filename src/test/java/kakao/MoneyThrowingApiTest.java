package kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import kakao.domain.ErrorCode;
import kakao.domain.Receiver;
import kakao.web.dto.ThrowingRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/18
 * Time : 6:59 AM
 */

@Slf4j
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MoneyThrowingApiTest {
    private static final String SERVER_URL = "http://localhost:8080/api/v1/money-throw";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private static final int throwCount  = 2;
    private static final int moneyAmount = 10000;
    private static final long throwerId  = 10101;
    private static final String roomId   = "room_01";
    private String accessToken = "";

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void 뿌리기() throws Exception {
        final ResultActions actions = throwMoney(throwerId, roomId);
        int resultCode = JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.resultCode");
        Assertions.assertEquals(ErrorCode.SUCCESS.getCode(), resultCode);
    }

    @Test
    void 받기() throws Exception {
        throwMoney(throwerId, roomId);

        long receiverId = 20202L;
        final ResultActions actions = mvc.perform(put(SERVER_URL + "/" + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("X-USER-ID", receiverId)
                .header("X-ROOM-iD", roomId)
        ).andExpect(status().isOk());

        int receiveMoney = JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.receiveMoneyAmount");
        Assertions.assertEquals(5000, receiveMoney);
        int resultCode = JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.resultCode");
        Assertions.assertEquals(ErrorCode.SUCCESS.getCode(), resultCode);
    }

    @Test
    void 조회하기() throws Exception {
        받기();

        final ResultActions actions = mvc.perform(get(SERVER_URL + "/" + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("X-USER-ID", throwerId)
                .header("X-ROOM-ID", roomId)
        ).andExpect(status().isOk());

        LocalDateTime throwingTime = LocalDateTime.parse(JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.throwingTime"));
        int throwingMoneyAmount = JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.throwingMoneyAmount");
        int receivedMoneyAmount = JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.receivedMoneyAmount");
        List<Receiver> receiverInfos = JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.receiverInfos");
        Gson gson = new Gson();
        List<Receiver> receivers = gson.fromJson(receiverInfos.toString(), new TypeToken<ArrayList<Receiver>>(){}.getType());

        Assertions.assertNotNull(throwingTime);
        Assertions.assertEquals(moneyAmount, throwingMoneyAmount);
        Assertions.assertEquals(5000, receivedMoneyAmount);
        Assertions.assertEquals(1, receivers.size());
        int resultCode = JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.resultCode");
        Assertions.assertEquals(ErrorCode.SUCCESS.getCode(), resultCode);
    }

    @Test
    void 다른사람이_조회하기() throws Exception {
        throwMoney(throwerId, roomId);

        ThrowingRequestDto requestDto = ThrowingRequestDto.builder().throwCount(10).moneyAmount(200000).build();
        final ResultActions actions = mvc.perform(get(SERVER_URL + "/" + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto))
                .header("X-USER-ID", 50505L)
                .header("X-ROOM-ID", roomId)
        ).andExpect(status().isOk());

        Assertions.assertEquals(ErrorCode.WRONG_SENDER.getCode(), (int)JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.resultCode"));
    }

    @Test
    void 뿌린사람이_받기() throws Exception {
        throwMoney(throwerId, roomId);

        final ResultActions actions = mvc.perform(put(SERVER_URL + "/" + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("X-USER-ID", throwerId)
                .header("X-ROOM-iD", roomId)
        ).andExpect(status().isOk());

        Assertions.assertEquals(ErrorCode.SENDER_CANNOT_RECEIVE_MONEY.getCode(), ((int) JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.resultCode")));
    }

    @Test
    void 뿌린사람_외에_다른사람이_잔액조회하기() throws Exception {
        throwMoney(throwerId, roomId);

        long otherId = 10103L;
        final ResultActions actions = checkBalance(otherId, roomId, accessToken);

        int resultCode = JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.resultCode");
        Assertions.assertEquals(ErrorCode.WRONG_SENDER.getCode(), resultCode);
    }

    private final ResultActions throwMoney(long userId, String roomId) throws Exception {
        ThrowingRequestDto requestDto = ThrowingRequestDto.builder().throwCount(throwCount).moneyAmount(moneyAmount).build();
        final ResultActions actions = mvc.perform(post(SERVER_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto))
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
        ).andExpect(status().isOk());

        accessToken = JsonPath.parse(actions.andReturn().getResponse().getContentAsString()).read("$.accessToken");
        Assertions.assertNotNull(accessToken);
        return actions;
    }

    private final ResultActions checkBalance(long userId, String roomId, String accessToken) throws Exception {
        return mvc.perform(get(SERVER_URL + "/" + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
        ).andExpect(status().isOk());
    }
}
