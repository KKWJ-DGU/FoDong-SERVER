package Fodong.serverdong.domain.wishlist.dto.request;

import Fodong.serverdong.domain.category.Category;
import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.restaurant.Restaurant;
import Fodong.serverdong.domain.wishlist.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class RequestWishlistCreationDto {
    public static Wishlist toEntity(Member member, Restaurant restaurant, Category category) {
        return Wishlist.builder()
                .restaurant(restaurant)
                .category(category)
                .member(member)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
