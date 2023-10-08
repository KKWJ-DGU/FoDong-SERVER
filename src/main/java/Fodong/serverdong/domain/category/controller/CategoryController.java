package Fodong.serverdong.domain.category.controller;

import Fodong.serverdong.domain.category.Category;
import Fodong.serverdong.domain.category.dto.response.ResponseCategoryListDto;
import Fodong.serverdong.domain.category.service.CategoryService;
import Fodong.serverdong.global.config.ApiDocumentResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiDocumentResponse
    @Operation( summary = "카테고리 리스트", description = "카테고리 리스트를 조회합니다.")
    @GetMapping("/list")
    public List<ResponseCategoryListDto> getCategoryList(){
        return categoryService.getCategoryList();
    }
}