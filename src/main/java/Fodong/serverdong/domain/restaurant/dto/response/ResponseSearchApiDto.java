package Fodong.serverdong.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ResponseSearchApiDto {

    List<Long> searchedCategoriesId;
    List<ResponseSearchRestaurantDto> searchRestaurantDtoList;
}
