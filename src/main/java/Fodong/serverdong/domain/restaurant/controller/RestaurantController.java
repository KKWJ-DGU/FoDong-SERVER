package Fodong.serverdong.domain.restaurant.controller;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantInfoDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseSearchRestaurantDto;
import Fodong.serverdong.global.config.ApiDocumentResponse;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public List<ResponseRestaurantDto> getRandomRestaurant(){
        return restaurantService.getRandomRestaurant();
    }

    @ApiDocumentResponse
    @Operation(summary = "카테고리 별 식당 리스트 조회",description = "카테고리에 해당하는 식당 리스트를 조회합니다.")
    @GetMapping("/category/{categoryId}")
    public List<ResponseRestaurantDto> getRestaurant(@PathVariable List<Long> categoryId){
         return restaurantService.getRestaurant(categoryId);
    }

    @ApiDocumentResponse
    @Operation(summary = "식당 정보 조회" , description = "식당 정보를 조회합니다.")
    @GetMapping("/info/{restaurantId}")
    public ResponseRestaurantInfoDto getRestaurantInfo(@PathVariable Long restaurantId){
        return restaurantService.getRestaurantInfo(restaurantId);
    }

    @ApiDocumentResponse
    @Operation(summary = "랜덤 식당 1개 조회" ,description = "식당 1개를 랜덤 조회합니다.")
    @GetMapping("/random/choice")
    public ResponseRestaurantDto getRestaurantChoice(){
        return restaurantService.getRandomRestaurantChoice();
    }
    @ApiDocumentResponse
    @Operation(summary = "검색 식당 조회",description = " 선택된 카테고리에 해당하는 식당을 조회합니다.")
    @GetMapping("/search/{categoryId}")
    public List<ResponseSearchRestaurantDto> getSearchRestaurant(@PathVariable List<Long> categoryId){
        return restaurantService.getSearchRestaurant(categoryId);
    }
}
