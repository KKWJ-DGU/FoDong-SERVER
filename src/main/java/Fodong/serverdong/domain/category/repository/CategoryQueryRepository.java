package Fodong.serverdong.domain.category.repository;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryListDto;

import java.util.List;

public interface CategoryQueryRepository {

    List<ResponseCategoryListDto> getCategoryList();
}
