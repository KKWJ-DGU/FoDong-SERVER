package Fodong.serverdong.domain.member.service;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.dto.response.ResponseMemberInfoDto;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    private final String testEmail = "testEmail@example.com";

    @BeforeEach
    void setup() {
        Member newTestMember = Member.builder()
                .email(testEmail)
                .nickname(testEmail)
                .build();
        memberRepository.save(newTestMember);
    }

    @Test
    @DisplayName("닉네임 설정")
    void setNicknameTest() {
        String newNickname = "시냥";

        // 닉네임 설정
        memberService.setNickname(testEmail, newNickname);

        Member updatedMember = memberRepository.findByEmail(testEmail).orElse(null);
        assertThat(updatedMember).isNotNull();
        assertThat(updatedMember.getNickname()).isEqualTo(newNickname);
    }

    @Test
    @DisplayName("닉네임 중복시 예외 발생")
    void setDuplicateNicknameTest() {
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.setNickname(testEmail, testEmail);
        });

        assertThat(exception.getMessage()).isEqualTo(new CustomException(CustomErrorCode.MEMBER_DUPLICATED_NICKNAME).getMessage());
    }

    @Test
    @DisplayName("회원 정보 조회")
    void getUserInfoTest() {

        Member testMember = memberRepository.findByEmail(testEmail).orElse(null);
        assertThat(testMember).isNotNull();

        ResponseMemberInfoDto memberInfo = memberService.getMemberInfo(testMember.getId());
        assertThat(memberInfo.getNickname()).isEqualTo(testEmail);

    }
}
