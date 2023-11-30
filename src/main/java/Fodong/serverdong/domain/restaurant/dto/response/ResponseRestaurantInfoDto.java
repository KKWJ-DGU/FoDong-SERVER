package Fodong.serverdong.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ResponseRestaurantInfoDto {

    private String name;
    private String address;
    private String imgUrl;
    private String categoryName;
    private String phoneNumber;
    private String webUrl;
    private List<ResponseMenuInfo> menuInfoList;
    private int wishCount;
    private Boolean wishState;


}
