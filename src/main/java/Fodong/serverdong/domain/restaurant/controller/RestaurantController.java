package Fodong.serverdong.domain.restaurant.controller;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantInfoDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseSearchRestaurantDto;
import Fodong.serverdong.global.auth.adapter.MemberAdapter;
import Fodong.serverdong.global.config.ApiDocumentResponse;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
@Tag(name="RestaurantController",description = "식당 API")
public class RestaurantController {

    private final RestaurantService restaurantService;
    @ApiDocumentResponse
    @Operation(summary = "랜덤 식당 리스트 조회", description = "랜덤으로 식당 리스트를 조회합니다.")
    @GetMapping("/random")
    public List<ResponseRestaurantDto> getRandomRestaurant(@AuthenticationPrincipal MemberAdapter memberAdapter){

        Long memberId = memberAdapter.getMember().getId();
        return restaurantService.getRandomRestaurant(memberId);

    }

    @ApiDocumentResponse
    @Operation(summary = "카테고리 별 식당 리스트 조회",description = "카테고리에 해당하는 식당 리스트를 조회합니다.")
    @GetMapping("/category/{categoryId}")
    public List<ResponseRestaurantDto> getRestaurant(@PathVariable Long categoryId , @AuthenticationPrincipal MemberAdapter memberAdapter){

        Long memberId = memberAdapter.getMember().getId();
        return restaurantService.getRestaurant(categoryId,memberId);
    }

    @ApiDocumentResponse
    @Operation(summary = "식당 정보 조회" , description = "식당 정보를 조회합니다.")
    @GetMapping("/info/{restaurantId}")
    public ResponseRestaurantInfoDto getRestaurantInfo(@PathVariable Long restaurantId , @AuthenticationPrincipal MemberAdapter memberAdapter){

        Long memberId = memberAdapter.getMember().getId();
        return restaurantService.getRestaurantInfo(restaurantId,memberId);
    }

    @ApiDocumentResponse
    @Operation(summary = "랜덤 식당 추천" ,description = "랜덤으로 식당을 추천해줍니다.")
    @GetMapping("/random/choice")
    public List<ResponseRestaurantDto> getRestaurantChoice(@AuthenticationPrincipal MemberAdapter memberAdapter){

        Long memberId = memberAdapter.getMember().getId();
        return restaurantService.getRandomRestaurantChoice(memberId);
    }
    @ApiDocumentResponse
    @Operation(summary = "검색 식당 조회",description = " 선택된 카테고리에 해당하는 식당을 조회합니다.")
    @GetMapping("/search/{categoryId}")
    public List<ResponseSearchRestaurantDto> getSearchRestaurant(@PathVariable List<Long> categoryId){
        return restaurantService.getSearchRestaurant(categoryId);
    }
}
