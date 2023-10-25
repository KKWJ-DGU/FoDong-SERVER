package Fodong.serverdong.domain.category.service;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryListDto;
import Fodong.serverdong.domain.category.repository.CategoryQueryRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryQueryRepositoryImpl categoryQueryRepository;

    /**
     * 카테고리 리스트 조회
     * @return 카테고리 리스트
     */
    @Transactional(readOnly = true)
    public Map<String,List<ResponseCategoryListDto>> getCategoryList() {

        return Collections.singletonMap("categoryList",categoryQueryRepository.getCategoryList());
    }
}
