package Fodong.serverdong.domain.restaurant.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRandomRestaurantDto;

import java.util.List;

public interface RestaurantQueryRepository {

    List<ResponseRandomRestaurantDto> getRandomRestaurant();
}
