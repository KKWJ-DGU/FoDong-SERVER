package Fodong.serverdong.domain.wishlist.dto.request;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.restaurantCategory.RestaurantCategory;
import Fodong.serverdong.domain.wishlist.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public class RequestWishlistCreationDto {
    public static Wishlist toEntity(Member member, RestaurantCategory restaurantCategory) {
        return Wishlist.builder()
                .restaurantCategory(restaurantCategory)
                .member(member)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
