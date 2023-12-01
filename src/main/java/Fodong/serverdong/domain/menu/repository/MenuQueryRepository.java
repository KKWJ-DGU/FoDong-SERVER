package Fodong.serverdong.domain.menu.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseMenuInfoListDto;

import java.util.List;

public interface MenuQueryRepository {

    List<ResponseMenuInfoListDto> getMenuInfoList(Long restaurantId);
}
