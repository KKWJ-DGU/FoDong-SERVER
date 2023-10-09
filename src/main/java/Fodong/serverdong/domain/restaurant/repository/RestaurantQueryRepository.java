package Fodong.serverdong.domain.restaurant.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantInfoDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseSearchRestaurantDto;

import java.util.List;

public interface RestaurantQueryRepository {

    List<ResponseRestaurantDto> getRandomRestaurant(Long memberId);

    List<ResponseRestaurantDto> getRestaurant(Long categoryId);

    ResponseRestaurantInfoDto getRestaurantInfo(Long productId);

    List<ResponseRestaurantDto> getRandomRestaurantChoice();

    List<ResponseSearchRestaurantDto> getSearchRestaurant(List<Long> categoryId);
}
