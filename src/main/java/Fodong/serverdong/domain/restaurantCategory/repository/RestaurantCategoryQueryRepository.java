package Fodong.serverdong.domain.restaurantCategory.repository;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryInfoListDto;

import java.util.List;

public interface RestaurantCategoryQueryRepository {

    List<ResponseCategoryInfoListDto> getRestaurantCategoryId(Long restaurantId);

}
