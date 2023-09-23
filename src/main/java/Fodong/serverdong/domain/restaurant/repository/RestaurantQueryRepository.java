package Fodong.serverdong.domain.restaurant.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantInfoDto;

import java.util.List;

public interface RestaurantQueryRepository {

    List<ResponseRestaurantDto> getRandomRestaurant();

    List<ResponseRestaurantDto> getRestaurant(List<Long> categoryId);

    ResponseRestaurantInfoDto getRestaurantInfo(Long productId);

    ResponseRestaurantDto getRandomRestaurantChoice();
}
