package Fodong.serverdong.domain.restaurant.controller;

import Fodong.serverdong.config.ApiDocumentResponse;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRandomRestaurantDto;
import Fodong.serverdong.domain.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    @Operation(summary = "랜덤 식당 정보 조회", description = "랜덤으로 식당 리스트를 조회합니다.")
    @GetMapping("/random")
    public List<ResponseRandomRestaurantDto> getRandomRestaurant(){
        return restaurantService.getRandomRestaurant();
    }
}
