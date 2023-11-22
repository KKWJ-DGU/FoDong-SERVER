package Fodong.serverdong.domain.wishlist.repository;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryListDto;
import Fodong.serverdong.domain.wishlist.dto.response.ResponseWishlistRestaurantDto;

import java.util.List;

public interface WishlistQueryRepository {
    List<ResponseWishlistRestaurantDto> getWishlistRestaurant(Long memberId, List<Long> categoryId);
    List<ResponseCategoryListDto> getWishlistCategory(Long memberId);

}
