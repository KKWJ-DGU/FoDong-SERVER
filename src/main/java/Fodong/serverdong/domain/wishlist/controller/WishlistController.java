package Fodong.serverdong.domain.wishlist.controller;

import Fodong.serverdong.domain.wishlist.service.WishlistService;
import Fodong.serverdong.global.auth.adapter.MemberAdapter;
import Fodong.serverdong.global.config.ApiDocumentResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
@Tag(name = "WishlistController", description = "위시리스트 API")
public class WishlistController {

    private final WishlistService wishlistService;

    @ApiDocumentResponse
    @Operation(summary = "위시리스트 추가", description = "식당을 위시리스트에 추가합니다.")
    @PostMapping("/{restaurantId}")
    public void addWishlist(@PathVariable Long restaurantId, @AuthenticationPrincipal MemberAdapter memberAdapter) {
        Long memberId = memberAdapter.getMember().getId();
        wishlistService.addWishlist(restaurantId, memberId);
    }

}
