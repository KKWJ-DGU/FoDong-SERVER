package Fodong.serverdong.domain.restaurant.controller;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRandomRestaurantDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantAllInfoDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseSearchApiDto;
import Fodong.serverdong.global.auth.adapter.MemberAdapter;
import Fodong.serverdong.global.config.ApiDocumentResponse;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
@Tag(name="RestaurantController",description = "식당 API")
public class RestaurantController {

    private final RestaurantService restaurantService;
    @ApiDocumentResponse
    @Operation(summary = "랜덤 식당 리스트 조회", description = "랜덤으로 식당 리스트를 조회합니다.")
    @GetMapping("/random")
    public Map<String,List<ResponseRandomRestaurantDto>> getRandomRestaurant(@AuthenticationPrincipal MemberAdapter memberAdapter){

        Long memberId = memberAdapter.getMember().getId();
        return restaurantService.getRandomRestaurant(memberId);

    }

    @ApiDocumentResponse
    @Operation(summary = "카테고리 별 식당 리스트 조회",description = "카테고리에 해당하는 식당 리스트를 조회합니다.")
    @GetMapping("/category/{categoryId}")
    public Map<String,List<ResponseRestaurantDto>> getRestaurant(@PathVariable Long categoryId , @AuthenticationPrincipal MemberAdapter memberAdapter){

        Long memberId = memberAdapter.getMember().getId();
        return restaurantService.getRestaurant(categoryId,memberId);
    }

    @ApiDocumentResponse
    @Operation(summary = "식당 정보 조회" , description = "식당 정보를 조회합니다.")
    @GetMapping("/info/{restaurantId}")
    public Map<String, ResponseRestaurantAllInfoDto> getRestaurantInfo(@PathVariable Long restaurantId , @AuthenticationPrincipal MemberAdapter memberAdapter){

        Long memberId = memberAdapter.getMember().getId();
        return restaurantService.getRestaurantInfo(restaurantId,memberId);
    }

    @ApiDocumentResponse
    @Operation(summary = "식당 랜덤 추천" ,description = "식당을 랜덤으로 추천해줍니다.")
    @GetMapping("/random/choice")
    public Map<String,List<ResponseRandomRestaurantDto>> getRestaurantChoice(@AuthenticationPrincipal MemberAdapter memberAdapter){

        Long memberId = memberAdapter.getMember().getId();
        return restaurantService.getRandomRestaurantChoice(memberId);
    }

    @ApiDocumentResponse
    @Operation(summary = "식당 검색",description = " 카테고리를 선택하여 식당을 검색합니다.")
    @GetMapping("/search")
    public ResponseSearchApiDto getSearchRestaurant
            (@RequestParam List<Long> categoryId , @AuthenticationPrincipal MemberAdapter memberAdapter) {

        Long memberId = memberAdapter.getMember().getId();
        return restaurantService.getSearchRestaurant(categoryId,memberId);


    }
}
