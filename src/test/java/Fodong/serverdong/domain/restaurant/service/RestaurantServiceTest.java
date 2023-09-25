package Fodong.serverdong.domain.restaurant.service;

import Fodong.serverdong.domain.restaurant.Restaurant;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantInfoDto;
import Fodong.serverdong.domain.restaurant.repository.RestaurantQueryRepositoryImpl;
import Fodong.serverdong.domain.restaurant.repository.RestaurantRepository;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
class RestaurantServiceTest {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    RestaurantQueryRepositoryImpl restaurantQueryRepository;

    @Test
    @DisplayName("랜덤 식당 리스트")
    void getRandomRestaurant() {
        List<ResponseRestaurantDto> randomRestaurantDtoList =restaurantQueryRepository.getRandomRestaurant();

        for(ResponseRestaurantDto randomRestaurantDto : randomRestaurantDtoList){

            log.info(randomRestaurantDto.getName());
            log.info(randomRestaurantDto.getCategoryName());
            log.info(randomRestaurantDto.getMenuName());
            log.info(randomRestaurantDto.getWishState().toString());
            log.info("===========================================");
        }
    }

    @Test
    @DisplayName("카테고리 별 식당 리스트")
    void getRestaurant() {
        List<Long> categoryId = new ArrayList<>();
        categoryId.add(1L);
        categoryId.add(2L);

        List<ResponseRestaurantDto> restaurantDtoList = restaurantQueryRepository.getRestaurant(categoryId);

        for(ResponseRestaurantDto restaurantDto : restaurantDtoList){

            log.info(restaurantDto.getName());
            log.info(restaurantDto.getCategoryName());
            log.info(restaurantDto.getMenuName());
            log.info(restaurantDto.getWishState().toString());
            log.info("===========================================");
        }

    }

    @Test
    @DisplayName("식당 정보 반환")
    void getRestaurantInfo(){

        Restaurant restaurant =
                new Restaurant(1L,"세븐일레븐","webUrl","031-000-0000","경기도","imgUrl",0);
        restaurantRepository.save(restaurant);

        restaurantRepository.findById(restaurant.getId()).orElseThrow(()-> new CustomException(CustomErrorCode.RESTAURANT_NOT_FOUND));

        ResponseRestaurantInfoDto responseRestaurantInfoDto =
                restaurantQueryRepository.getRestaurantInfo(restaurant.getId());

        log.info(responseRestaurantInfoDto.getName());
        log.info(responseRestaurantInfoDto.getCategoryName());
        log.info(responseRestaurantInfoDto.getMenu());
        log.info(responseRestaurantInfoDto.getPhoneNumber());

    }

    @Test
    @DisplayName("랜덤 식당 1개 반환")
    void getRandomRestaurantChoice(){
        ResponseRestaurantDto restaurantDto =
                restaurantQueryRepository.getRandomRestaurantChoice();

        log.info(restaurantDto.getName());
        log.info(restaurantDto.getMenuName());
        log.info(restaurantDto.getCategoryName());
        log.info(restaurantDto.getImgUrl());
        log.info(String.valueOf(restaurantDto.getWishCount()));
        log.info(String.valueOf(restaurantDto.getWishState()));
    }
}