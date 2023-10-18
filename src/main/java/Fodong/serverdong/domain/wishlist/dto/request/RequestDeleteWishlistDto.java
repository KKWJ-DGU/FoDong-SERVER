package Fodong.serverdong.domain.wishlist.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDeleteWishlistDto {
    private Long restaurantId;
}
