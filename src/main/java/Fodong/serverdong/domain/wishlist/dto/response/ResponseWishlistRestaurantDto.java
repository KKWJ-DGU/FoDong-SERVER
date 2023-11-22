package Fodong.serverdong.domain.wishlist.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseWishlistRestaurantDto {

    private Long id;
    private String name;
    private String imgUrl;
    private String categoryId;
    private String categoryName;
    private String menuName;
    private int wishCount;
    private Boolean wishState;
}