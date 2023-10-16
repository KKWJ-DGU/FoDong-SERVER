package Fodong.serverdong.domain.wishlist.service;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.MemberToken;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseTokenDto;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.domain.wishlist.Wishlist;
import Fodong.serverdong.domain.wishlist.repository.WishlistRepository;
import Fodong.serverdong.global.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class WishlistServiceTest {

    @Autowired
    JwtService jwtService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberTokenRepository memberTokenRepository;
    @Autowired
    WishlistRepository wishlistRepository;
    @Autowired
    MockMvc mockMvc;

    private MemberToken testMemberToken;

    @BeforeEach
    void setup() {
        Member testMember = Member.builder()
                .email("testEmail@exmaple.com")
                .nickname("Test Nickname")
                .build();

        memberRepository.save(testMember);

        ResponseTokenDto accessTokenDto = jwtService.createAccessToken(testMember.getEmail());
        ResponseTokenDto refreshTokenDto = jwtService.createRefreshToken();

        testMemberToken = MemberToken.builder()
                .member(testMember)
                .accessToken(accessTokenDto.getToken())
                .accessExpiration(accessTokenDto.getExpiryDate())
                .refreshToken(refreshTokenDto.getToken())
                .refreshExpiration(refreshTokenDto.getExpiryDate())
                .build();

        memberTokenRepository.save(testMemberToken);
    }

    @Test
    @DisplayName("위시리스트 추가")
    void addWishlistTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.post("/api/wishlist/1")
                        .header("Authorization", "Bearer "+ testMemberToken.getAccessToken()))
                .andExpect(status().isOk());

        List<Wishlist> wishlists = wishlistRepository.findByMember(testMemberToken.getMember());
        assertThat(wishlists).isNotEmpty();
    }
}