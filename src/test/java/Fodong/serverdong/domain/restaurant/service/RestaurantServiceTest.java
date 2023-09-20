package Fodong.serverdong.domain.restaurant.service;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRandomRestaurantDto;
import Fodong.serverdong.domain.restaurant.repository.RestaurantQueryRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
class RestaurantServiceTest {

    @Autowired
    RestaurantQueryRepositoryImpl restaurantQueryRepository;

    @Test
    @DisplayName("랜덤 식당 리스트")
    void getRestaurant() {
        List<ResponseRandomRestaurantDto> randomRestaurantDtoList =restaurantQueryRepository.getRandomRestaurant();

        for(ResponseRandomRestaurantDto restaurantDto : randomRestaurantDtoList){

            log.info(restaurantDto.getName());
            log.info(restaurantDto.getCategoryName());
            log.info(restaurantDto.getMenuName());
            log.info(restaurantDto.getWishState().toString());
            log.info("===========================================");
        }
    }
}