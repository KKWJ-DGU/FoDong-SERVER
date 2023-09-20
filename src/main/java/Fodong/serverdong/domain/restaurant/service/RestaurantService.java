package Fodong.serverdong.domain.restaurant.service;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRandomRestaurantDto;
import Fodong.serverdong.domain.restaurant.repository.RestaurantQueryRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantQueryRepositoryImpl restaurantQueryRepository;

    @Transactional
    public List<ResponseRandomRestaurantDto> getRandomRestaurant() {

        return restaurantQueryRepository.getRandomRestaurant();

    }
}
