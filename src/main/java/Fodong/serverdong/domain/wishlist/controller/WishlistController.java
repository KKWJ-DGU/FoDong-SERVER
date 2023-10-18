package Fodong.serverdong.domain.wishlist.controller;

import Fodong.serverdong.domain.wishlist.dto.request.RequestAddWishlistDto;
import Fodong.serverdong.domain.wishlist.dto.request.RequestDeleteWishlistDto;
import Fodong.serverdong.domain.wishlist.service.WishlistService;
import Fodong.serverdong.global.auth.adapter.MemberAdapter;
import Fodong.serverdong.global.config.ApiDocumentResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
@Tag(name = "WishlistController", description = "위시리스트 API")
public class WishlistController {

    private final WishlistService wishlistService;

    @ApiDocumentResponse
    @Operation(summary = "위시리스트 추가", description = "식당을 위시리스트에 추가합니다.")
    @PostMapping
    public void addWishlist(@RequestBody RequestAddWishlistDto addDto, @AuthenticationPrincipal MemberAdapter memberAdapter) {
        Long memberId = memberAdapter.getMember().getId();
        wishlistService.addWishlist(addDto.getRestaurantId(), memberId);
    }

    @ApiDocumentResponse
    @Operation(summary = "위시리스트 삭제", description = "식당을 위시리스트에서 삭제합니다.")
    @DeleteMapping
    public void deleteWishlist(@RequestBody RequestDeleteWishlistDto deleteDto, @AuthenticationPrincipal MemberAdapter memberAdapter) {
        Long memberId = memberAdapter.getMember().getId();
        wishlistService.deleteWishlist(deleteDto.getRestaurantId(), memberId);
    }


}