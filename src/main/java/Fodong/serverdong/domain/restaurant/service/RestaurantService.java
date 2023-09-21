package Fodong.serverdong.domain.restaurant.service;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.repository.RestaurantQueryRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final RestaurantQueryRepositoryImpl restaurantQueryRepository;

    /**
     * 랜덤 식당 리스트 조회
     */
    @Transactional
    public List<ResponseRestaurantDto> getRandomRestaurant() {
        return restaurantQueryRepository.getRandomRestaurant();
    }

    /**
     * 카테고리 별 식당 리스트 조회
     */
    @Transactional
    public List<ResponseRestaurantDto> getRestaurant(List<Long> categoryId) {
        return restaurantQueryRepository.getRestaurant(categoryId);
    }
}
