package kakao.web;

import kakao.domain.ErrorCode;
import kakao.service.MoneyThrowingService;
import kakao.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/18
 * Time : 6:04 AM
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class MoneyThrowingApiController {
    private final MoneyThrowingService moneyThrowingService;

    @PostMapping("/api/v1/money-throw")
    public ThrowingResponseDto throwing(HttpServletRequest request, @RequestBody ThrowingRequestDto requestDto) {
        long userId = getMemberId(request);
        String roomId = getRoomId(request);
        log.info("[throwing] userId: <{}>, roomId:<{}>", userId, roomId);
        log.info("[throwing] moneyAmount: <{}>, throwCount:<{}>", requestDto.getMoneyAmount(), requestDto.getThrowCount());
        ThrowingResponseDto responseDto = moneyThrowingService.throwMoney(requestDto, roomId, userId);
        return responseDto;
    }

    @GetMapping("/api/v1/money-throw/{accessToken}")
    public CheckBalanceResponseDto checkBalance(HttpServletRequest request, @PathVariable(name = "accessToken") String accessToken) {
        log.info("[checkBalance] accessToken: <{}>", accessToken);
        CheckBalanceRequestDto requestDto = CheckBalanceRequestDto.builder()
                .accessToken(accessToken)
                .roomId(getRoomId(request))
                .senderId(getMemberId(request))
                .build();
        CheckBalanceResponseDto responseDto;
        try {
            responseDto = moneyThrowingService.checkBalance(requestDto);
        } catch (IllegalArgumentException e) {
           e.printStackTrace();
           responseDto = new CheckBalanceResponseDto(ErrorCode.WRONG_ACCESS_TOKEN);
        }
        return responseDto;
    }

    @PutMapping("/api/v1/money-throw/{accessToken}")
    public MoneyReceiveResponseDto receiveMoney(HttpServletRequest request, @PathVariable(name = "accessToken") String accessToken) {
        log.info("[receiveMoney] accessToken: <{}>", accessToken);
        MoneyReceiveRequestDto requestDto = MoneyReceiveRequestDto.builder()
                .accessToken(accessToken)
                .roomId(getRoomId(request))
                .receiverId(getMemberId(request))
                .build();
        MoneyReceiveResponseDto responseDto;
        try {
            responseDto = moneyThrowingService.receiveMoney(requestDto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            responseDto = new MoneyReceiveResponseDto(ErrorCode.WRONG_ACCESS_TOKEN);
        }
        return responseDto;
    }

    private String getRoomId(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-ROOM-ID")).orElseThrow(() -> new IllegalArgumentException("Wrong X-ROOM-ID"));
    }

    private long getMemberId(HttpServletRequest request) {
        return Long.parseLong(Optional.ofNullable(request.getHeader("X-USER-ID")).orElseThrow(() -> new IllegalArgumentException("Wrong X-USER-ID")));
    }
}
