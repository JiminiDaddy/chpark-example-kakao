package kakao.service;

import kakao.domain.*;
import kakao.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
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

    private final RoomRepository roomRepository;

    private final MemberRepository memberRepository;

    // TODO DATABASE에서 관리하도록!
    private Set<String> accessTokens = new HashSet<>();

    @Transactional
    public String throwMoney(ThrowingRequestDto requestDto, String roomId, long senderId) {
        // TODO Validation을 Controller에서 공통 처리할것인지 도메인 영역으로 보낼것인지 고민중 (후자가 맞을듯)
        //if (!isValidRoom(roomId)) throw new IllegalArgumentException("Wrong roomId: <" + roomId + ">");
        //if (!isValidMember(roomId, senderId)) throw new IllegalArgumentException("Wrong senderId: <" + senderId + ">");

        String accessToken = createAccessToken();
        MoneyThrowing moneyThrowing = MoneyThrowing.builder()
                .accessToken(accessToken)
                .moneyAmount(requestDto.getMoneyAmount())
                .throwCount(requestDto.getThrowCount())
                .roomId(roomId)
                .senderId(senderId)
                .build();

        moneyThrowing = moneyThrowingRepository.save(moneyThrowing);
        return moneyThrowing.getAccessToken();
    }

    @Transactional
    public CheckBalanceResponseDto checkBalance(CheckBalanceRequestDto requestDto) {
        MoneyThrowing moneyThrowing = getMoneyThrowing(requestDto.getAccessToken());
        // TODO mone¥Throwing에서 Room/Sender 유효성 체크 해야함
        // TODO 조회 로직 구현
        CheckBalanceResponseDto responseDto = new CheckBalanceResponseDto(moneyThrowing);
        return responseDto;
    }

    @Transactional
    public MoneyReceiveResponseDto receiveMoney(MoneyReceiveRequestDto requestDto) {
        MoneyThrowing moneyThrowing = getMoneyThrowing(requestDto.getAccessToken());
        // TODO mone¥Throwing에서 Room/Sender 유효성 체크 해야함
        Member receiver = getMember(requestDto.getReceiverId());
        int receiveMoneyAmount = moneyThrowing.receiveMoney(receiver);
        MoneyReceiveResponseDto responseDto = MoneyReceiveResponseDto.builder()
                .receiveMoneyAmount(receiveMoneyAmount).build();
       return responseDto;
      // return MoneyReceiveResponseDto.builder().receiveMoneyAmount(1000).build();
    }

    @Transactional
    public int test() {
       return 500;
    }

    /*
    @Transactional
    private boolean isValidRoom(String roomId) {
        return roomRepository.findById(roomId).isPresent();
    }

    private boolean isValidMember(String roomId, int memberId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Wrong roomId: <" + roomId + ">"));
        Set<Member> members = room.getMembers();
        return members != null && !members.isEmpty() && members.contains(memberId);
    }
    */

    private String createAccessToken() {
        String accessToken = "";
        while (true) {
            accessToken = UUID.randomUUID().toString().substring(0, 3);
            if (!accessTokens.contains(accessToken)) {
                accessTokens.add(accessToken);
                break;
            }
        }
        return accessToken;
    }

    private MoneyThrowing getMoneyThrowing(String accessToken) {
        return moneyThrowingRepository.findById(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("Wrong accessToken: <" + accessToken + ">"));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Wrong memberId: <" + memberId + ">"));
    }
}
