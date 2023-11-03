package Fodong.serverdong.domain.wishlist.repository;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryListDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;

import java.util.List;

public interface WishlistQueryRepository {
    List<ResponseRestaurantDto> getWishlistRestaurant(Long memberId, Long categoryId);
    List<ResponseCategoryListDto> getWishlistCategory(Long memberId);

}
