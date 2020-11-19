package kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.web.dto.ThrowingRequestDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/18
 * Time : 6:59 AM
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoneyThrowingApiTest {
    private static final String SERVER_URL = "http://localhost:8080/api/v1/money-throw";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void 뿌리기() throws Exception {
        int throwCount = 2;
        int moneyAmount = 10000;
        long  throwerId = 1010101;
        String roomId = "room_01";
        ThrowingRequestDto requestDto = ThrowingRequestDto.builder().throwCount(throwCount).moneyAmount(moneyAmount).build();

        mvc.perform(post(SERVER_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto))
                .header("X-USER-ID", throwerId)
                .header("X-ROOM-ID", roomId)
        ).andExpect(status().isOk());
    }
}
