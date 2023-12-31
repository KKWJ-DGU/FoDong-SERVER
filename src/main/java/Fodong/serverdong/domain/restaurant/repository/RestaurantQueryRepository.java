package Fodong.serverdong.domain.restaurant.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRandomRestaurantDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantBasicInfoDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseSearchRestaurantDto;

import java.util.List;

public interface RestaurantQueryRepository {

    List<ResponseRandomRestaurantDto> getRandomRestaurant(Long memberId);

    List<ResponseRestaurantDto> getRestaurant(Long categoryId,Long memberId);

    List<ResponseRestaurantBasicInfoDto> getRestaurantInfo(Long restaurantId, Long memberId);

    List<ResponseRandomRestaurantDto> getRandomRestaurantChoice(Long memberId);

    List<ResponseSearchRestaurantDto> getSearchRestaurant(List<Long> categoryId,Long memberId);
}
