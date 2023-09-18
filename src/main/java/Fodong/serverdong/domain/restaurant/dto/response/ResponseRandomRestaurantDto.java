package Fodong.serverdong.domain.restaurant.dto.response;

import Fodong.serverdong.domain.restaurant.Restaurant;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ResponseRandomRestaurantDto {

    private String name;
    private String imgUrl;
//    private List<String> category;
//    private List<String> menu;
    private int wishCount;

}
