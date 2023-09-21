package Fodong.serverdong.domain.restaurant.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;

import java.util.List;

public interface RestaurantQueryRepository {

    List<ResponseRestaurantDto> getRandomRestaurant();

    List<ResponseRestaurantDto> getRestaurant(List<Long> categoryId);
}
