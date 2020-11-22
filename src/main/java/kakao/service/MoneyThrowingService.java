package kakao.service;

import kakao.domain.MoneyThrowing;
import kakao.domain.MoneyThrowingRepository;
import kakao.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/19
 * Time : 7:16 AM
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class MoneyThrowingService {
    private final MoneyThrowingRepository moneyThrowingRepository;

    @Transactional
    public ThrowingResponseDto throwMoney(ThrowingRequestDto requestDto, String roomId, long senderId) {
        String accessToken = createAccessToken();
        MoneyThrowing moneyThrowing = MoneyThrowing.builder()
                .accessToken(accessToken)
                .moneyAmount(requestDto.getMoneyAmount())
                .throwCount(requestDto.getThrowCount())
                .roomId(roomId)
                .senderId(senderId)
                .build();

        moneyThrowing = moneyThrowingRepository.save(moneyThrowing);
        ThrowingResponseDto responseDto = new ThrowingResponseDto(moneyThrowing.getErrorCode(), accessToken);
        return responseDto;
    }

    @Transactional
    public CheckBalanceResponseDto checkBalance(CheckBalanceRequestDto requestDto) {
        MoneyThrowing moneyThrowing = getMoneyThrowing(requestDto.getAccessToken());
        moneyThrowing.checkBalance(requestDto.getRoomId(), requestDto.getSenderId());
        CheckBalanceResponseDto responseDto = new CheckBalanceResponseDto(moneyThrowing);
        return responseDto;
    }

    @Transactional
    public MoneyReceiveResponseDto receiveMoney(MoneyReceiveRequestDto requestDto) {
        MoneyThrowing moneyThrowing = getMoneyThrowing(requestDto.getAccessToken());
        int receiveMoneyAmount = moneyThrowing.receiveMoney(requestDto.getRoomId(), requestDto.getReceiverId());
        MoneyReceiveResponseDto responseDto = new MoneyReceiveResponseDto(moneyThrowing.getErrorCode(), receiveMoneyAmount);
       return responseDto;
    }

    private String createAccessToken() {
        String accessToken = "";
        while(true) {
            accessToken = UUID.randomUUID().toString().substring(0, 3);
            if (!moneyThrowingRepository.existsById(accessToken)) {
                break;
            }
        }
        return accessToken;
    }

    private MoneyThrowing getMoneyThrowing(String accessToken) {
        return moneyThrowingRepository.findById(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("Wrong accessToken: <" + accessToken + ">"));
    }
}
