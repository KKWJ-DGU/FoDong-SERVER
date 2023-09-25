package Fodong.serverdong.domain.category.service;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryListDto;
import Fodong.serverdong.domain.category.repository.CategoryQueryRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryQueryRepositoryImpl categoryQueryRepository;
    @Transactional
    public List<ResponseCategoryListDto> getCategoryList() {

        return categoryQueryRepository.getCategoryList();
    }
}
