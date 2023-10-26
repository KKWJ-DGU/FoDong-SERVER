package Fodong.serverdong.domain.restaurant.service;

import Fodong.serverdong.domain.category.repository.CategoryRepository;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantInfoDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseSearchApiDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseSearchRestaurantDto;
import Fodong.serverdong.domain.restaurant.repository.RestaurantQueryRepositoryImpl;
import Fodong.serverdong.domain.restaurant.repository.RestaurantRepository;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantQueryRepositoryImpl restaurantQueryRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 랜덤 식당 리스트 조회
     * @param memberId 회원 ID
     * @return 랜덤 식당 리스트
     */
    public Map<String,List<ResponseRestaurantDto>> getRandomRestaurant(Long memberId) {
        return Collections.singletonMap("randomRestaurantList",restaurantQueryRepository.getRandomRestaurant(memberId));
    }

    /**
     * 카테고리 별 식당 리스트 조회
     * @param categoryId 카테고리 ID
     * @param memberId 회원 ID
     * @return 카테고리 별 식당 리스트
     */
    public Map<String,List<ResponseRestaurantDto>> getRestaurant(Long categoryId,Long memberId) {

        categoryRepository.findById(categoryId).orElseThrow(()-> new CustomException(CustomErrorCode.CATEGORY_NOT_FOUND));
        return Collections.singletonMap("restaurantByCategory",restaurantQueryRepository.getRestaurant(categoryId,memberId));
    }

    /**
     * 식당 정보 조회
     * @param restaurantId 식당 ID
     * @param memberId 회원 ID
     * @return 식당 정보
     */
    public ResponseRestaurantInfoDto getRestaurantInfo(Long restaurantId,Long memberId) {
        restaurantRepository.findById(restaurantId).orElseThrow(()->new CustomException(CustomErrorCode.RESTAURANT_NOT_FOUND));

        return restaurantQueryRepository.getRestaurantInfo(restaurantId,memberId );
    }

    /**
     * 식당 랜덤 추천
     * @param memberId 회원 ID
     * @return 추천 식당 리스트
     */
    public Map<String,List<ResponseRestaurantDto>> getRandomRestaurantChoice(Long memberId) {

        return Collections.singletonMap("recommendedRestaurant",restaurantQueryRepository.getRandomRestaurantChoice(memberId));
    }


    /**
     * 식당 검색
     * @param categoryId 카테고리 ID
     * @param memberId 회원 Id
     * @return 검색된 식당 리스트
     */
    public ResponseSearchApiDto getSearchRestaurant(List<Long> categoryId, Long memberId) {
        categoryId.forEach(category ->
                categoryRepository.findById(category).orElseThrow(()-> new CustomException(CustomErrorCode.CATEGORY_NOT_CONTAIN)));

        List<ResponseSearchRestaurantDto> searchRestaurant = restaurantQueryRepository.getSearchRestaurant(categoryId,memberId);

        HashSet<String> requestId = new HashSet<>();
        categoryId.forEach(cate -> requestId.add(String.valueOf(cate)));

        List<ResponseSearchRestaurantDto> getCategoryRestaurant = new ArrayList<>();

        for(ResponseSearchRestaurantDto restaurantDto : searchRestaurant){
            HashSet<String> searchId = new HashSet<>(List.of(restaurantDto.getCategoryId().split(",")));

            if(searchId.equals(requestId)){
                getCategoryRestaurant.add(restaurantDto);
            }
        }

        return new ResponseSearchApiDto(categoryId,getCategoryRestaurant);
    }
}
