package Fodong.serverdong.domain.restaurant.dto.response;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryInfoListDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ResponseRandomRestaurantDto {

    private Long restaurantId;
    private String name;
    private String imgUrl;
    private List<ResponseCategoryInfoListDto> categoryInfo;
    private String menuName;
    private int wishCount;
    private Boolean wishState;

}
