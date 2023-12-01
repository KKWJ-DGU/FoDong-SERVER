package Fodong.serverdong.domain.category.service;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryInfoListDto;
import Fodong.serverdong.domain.category.repository.CategoryQueryRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;


@SpringBootTest
@Slf4j
@Transactional
class CategoryServiceTest {

    @Autowired
    CategoryQueryRepositoryImpl categoryQueryRepository;

    @Test
    @DisplayName("카테고리 리스트 조회")
    void CategoryList(){
        List<ResponseCategoryInfoListDto> categoryList = categoryQueryRepository.getCategoryList();

        for(ResponseCategoryInfoListDto category : categoryList){
            log.info(String.valueOf(category.getId()));
            log.info(category.getCategoryName());
            log.info(category.getCategoryImg());
    }
    }
}