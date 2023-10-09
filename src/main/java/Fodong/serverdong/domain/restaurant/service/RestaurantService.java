package Fodong.serverdong.domain.restaurant.service;

import Fodong.serverdong.domain.category.repository.CategoryRepository;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantInfoDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseSearchRestaurantDto;
import Fodong.serverdong.domain.restaurant.repository.RestaurantQueryRepositoryImpl;
import Fodong.serverdong.domain.restaurant.repository.RestaurantRepository;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final RestaurantQueryRepositoryImpl restaurantQueryRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 랜덤 식당 리스트 조회
     */
    @Transactional
    public List<ResponseRestaurantDto> getRandomRestaurant(Long memberId) {
        return restaurantQueryRepository.getRandomRestaurant(memberId);
    }

    /**
     * 카테고리 별 식당 리스트 조회
     */
    @Transactional
    public List<ResponseRestaurantDto> getRestaurant(Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(()-> new CustomException(CustomErrorCode.CATEGORY_NOT_FOUND));
        return restaurantQueryRepository.getRestaurant(categoryId);
    }

    /**
     * 식당 정보 조회
     */
    @Transactional
    public ResponseRestaurantInfoDto getRestaurantInfo(Long restaurantId) {
        restaurantRepository.findById(restaurantId).orElseThrow(()->new CustomException(CustomErrorCode.RESTAURANT_NOT_FOUND));

        return restaurantQueryRepository.getRestaurantInfo(restaurantId);
    }

    /**
     * 랜덤 식당 1개 조회
     */
    @Transactional
    public List<ResponseRestaurantDto> getRandomRestaurantChoice() {

        return restaurantQueryRepository.getRandomRestaurantChoice();
    }

    @Transactional
    public List<ResponseSearchRestaurantDto> getSearchRestaurant(List<Long> categoryId) {
        categoryId.forEach(category ->
                categoryRepository.findById(category).orElseThrow(()-> new CustomException(CustomErrorCode.CATEGORY_NOT_CONTAIN)));

        List<ResponseSearchRestaurantDto> searchRestaurant = restaurantQueryRepository.getSearchRestaurant(categoryId);

        HashSet<String> requestId = new HashSet<>();
        categoryId.forEach(cate -> requestId.add(String.valueOf(cate)));

        List<ResponseSearchRestaurantDto> getCategoryRestaurant = new ArrayList<>();

        for(ResponseSearchRestaurantDto restaurantDto : searchRestaurant){
            HashSet<String> searchId = new HashSet<>(List.of(restaurantDto.getCategoryId().split(",")));

            if(searchId.containsAll(requestId)){
                getCategoryRestaurant.add(restaurantDto);
            }
        }

        return getCategoryRestaurant;
    }
}
