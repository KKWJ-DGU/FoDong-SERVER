package Fodong.serverdong.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseRestaurantInfoDto {

    private String name;
    private String imgUrl;
    private String categoryName;
    private String phoneNumber;
    private String webUrl;
    private String menu;
    private int wishCount;
    private Boolean wishState;


}
