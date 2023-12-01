package Fodong.serverdong.domain.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseCategoryInfoListDto {

    private Long id;
    private String categoryName;
    private String categoryImg;
}
