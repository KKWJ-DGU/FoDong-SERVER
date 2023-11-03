package Fodong.serverdong.domain.wishlist.service;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.MemberToken;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseTokenDto;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.domain.wishlist.dto.request.RequestAddWishlistDto;
import Fodong.serverdong.global.auth.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

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

//    @Test
//    @DisplayName("위시리스트 추가")
//    void addWishlistTest() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestBody = objectMapper.writeValueAsString(new RequestAddWishlistDto(1L));
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/wishlist")
//                        .header("Authorization", "Bearer " + testMemberToken.getAccessToken())
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("위시리스트 삭제")
//    void deleteWishlistTest() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestBody = objectMapper.writeValueAsString(new RequestAddWishlistDto(1L));
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/wishlist")
//                        .header("Authorization", "Bearer " + testMemberToken.getAccessToken())
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
    @Test
    @DisplayName("위시리스트 전체 조회")
    void getWishlistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/wishlist")
                        .header("Authorization", "Bearer " + testMemberToken.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("위시리스트 카테고리 조회")
    void getWishlistCategoryTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/wishlist/category")
                        .header("Authorization", "Bearer " + testMemberToken.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}