package Fodong.serverdong.domain.wishlist.repository;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryListDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseSearchRestaurantDto;

import java.util.List;

public interface WishlistQueryRepository {
    List<ResponseSearchRestaurantDto> getWishlistRestaurant(Long memberId, List<Long> categoryId);
    List<ResponseCategoryListDto> getWishlistCategory(Long memberId);

}
