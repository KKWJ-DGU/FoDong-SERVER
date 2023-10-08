package Fodong.serverdong.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseSearchRestaurantDto {


    private String name;
    private String imgUrl;
    private String categoryId;
    private String categoryName;
    private String menuName;
    private int wishCount;
    private Boolean wishState;


}
