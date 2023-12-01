package Fodong.serverdong.domain.category.repository;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryInfoListDto;

import java.util.List;

public interface CategoryQueryRepository {

    List<ResponseCategoryInfoListDto> getCategoryList();

}
