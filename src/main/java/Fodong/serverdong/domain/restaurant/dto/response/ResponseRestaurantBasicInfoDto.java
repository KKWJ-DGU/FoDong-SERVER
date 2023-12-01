package Fodong.serverdong.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseRestaurantBasicInfoDto {

    private String name;
    private String address;
    private String imgUrl;
    private String phoneNumber;
    private String webUrl;
    private int wishCount;
    private Boolean wishState;


}
