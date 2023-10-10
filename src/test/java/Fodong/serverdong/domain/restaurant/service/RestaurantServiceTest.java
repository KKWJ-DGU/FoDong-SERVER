package Fodong.serverdong.domain.restaurant.service;

import Fodong.serverdong.domain.category.Category;
import Fodong.serverdong.domain.category.repository.CategoryQueryRepositoryImpl;
import Fodong.serverdong.domain.category.repository.CategoryRepository;
import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.memberToken.MemberToken;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseTokenDto;
import Fodong.serverdong.domain.memberToken.repository.MemberTokenRepository;
import Fodong.serverdong.domain.restaurant.repository.RestaurantQueryRepositoryImpl;
import Fodong.serverdong.domain.restaurant.repository.RestaurantRepository;
import Fodong.serverdong.global.auth.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@Transactional
@AutoConfigureMockMvc
class RestaurantServiceTest {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    RestaurantQueryRepositoryImpl restaurantQueryRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberTokenRepository memberTokenRepository;
    @Autowired
    CategoryQueryRepositoryImpl categoryQueryRepository;
    @Autowired
    MockMvc mockMvc;
    private MemberToken testMemberToken;
    private Category category;

    @BeforeEach
    void setup() {
        Member testMember = Member.builder()
                .email("codms7020")
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
    @DisplayName("랜덤 식당 리스트")
    void getRandomRestaurant() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurant/random")
                        .header("Authorization", "Bearer "+testMemberToken.getAccessToken()))
                .andExpect(status().isOk());

    }

//    @Test
//    @DisplayName("카테고리 별 식당 리스트")
//    void getRestaurant() throws Exception {
//
//        Long categoryId = categoryQueryRepository.getCategoryId();;
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurant/category/"+categoryId)
//                        .header("Authorization", "Bearer "+testMemberToken.getAccessToken()))
//                .andExpect(status().isOk());
//
//    }

//    @Test
//    @DisplayName("식당 정보 반환")
//    void getRestaurantInfo(){
//
//        Restaurant restaurant =
//                new Restaurant(1L,"세븐일레븐","webUrl","031-000-0000","경기도","imgUrl",0);
//        restaurantRepository.save(restaurant);
//
//        restaurantRepository.findById(restaurant.getId()).orElseThrow(()-> new CustomException(CustomErrorCode.RESTAURANT_NOT_FOUND));
//
//        ResponseRestaurantInfoDto responseRestaurantInfoDto =
//                restaurantQueryRepository.getRestaurantInfo(restaurant.getId());
//
//        log.info(responseRestaurantInfoDto.getName());
//        log.info(responseRestaurantInfoDto.getCategoryName());
//        log.info(responseRestaurantInfoDto.getMenu());
//        log.info(responseRestaurantInfoDto.getPhoneNumber());
//
//    }

//    @Test
//    @DisplayName("랜덤 식당 1개 반환")
//    void getRandomRestaurantChoice(){
//        List<ResponseRestaurantDto> responseRestaurantDtos =
//                restaurantQueryRepository.getRandomRestaurantChoice();
//
//        for(ResponseRestaurantDto restaurantDto : responseRestaurantDtos){
//            log.info(restaurantDto.getName());
//            log.info(restaurantDto.getMenuName());
//            log.info(restaurantDto.getCategoryName());
//            log.info(restaurantDto.getImgUrl());
//            log.info(String.valueOf(restaurantDto.getWishCount()));
//            log.info(String.valueOf(restaurantDto.getWishState()));
//        }
//
//    }
//
//    @Test
//    @DisplayName("검색 식당 조회")
//    void getSearchRestaurant(){
//
//        List<Long> categoryId = new ArrayList<>();
//        categoryId.add(1L);
//        categoryId.add(3L);
//
//
//        List<ResponseSearchRestaurantDto> searchRestaurant = restaurantQueryRepository.getSearchRestaurant(categoryId);
//
//        HashSet<String> requestId = new HashSet<>();
//        categoryId.forEach(cate -> requestId.add(String.valueOf(cate)));
//
//        List<ResponseSearchRestaurantDto> getCategoryRestaurant = new ArrayList<>();
//
//        for(ResponseSearchRestaurantDto restaurantDto : searchRestaurant){
//            HashSet<String> searchId = new HashSet<>(List.of(restaurantDto.getCategoryId().split(",")));
//
//            if(searchId.containsAll(requestId)){
//                getCategoryRestaurant.add(restaurantDto);
//            }
//        }
//
//        for(ResponseSearchRestaurantDto searchRestaurantDto : getCategoryRestaurant){
//            log.info(searchRestaurantDto.getCategoryId());
//            log.info(searchRestaurantDto.getCategoryName());
//            log.info(searchRestaurantDto.getName());
//            log.info(searchRestaurantDto.getMenuName());
//            log.info(String.valueOf(searchRestaurantDto.getWishCount()));
//            log.info(String.valueOf(searchRestaurantDto.getWishState()));
//            log.info("===========================================");
//
//        }
//    }
}