package Fodong.serverdong.domain.restaurant.service;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.repository.RestaurantQueryRepositoryImpl;
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
}