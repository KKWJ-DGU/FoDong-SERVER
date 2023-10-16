package Fodong.serverdong.domain.wishlist.service;

import Fodong.serverdong.domain.category.Category;
import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.restaurant.Restaurant;
import Fodong.serverdong.domain.restaurant.repository.RestaurantRepository;
import Fodong.serverdong.domain.restaurantCategory.RestaurantCategory;
import Fodong.serverdong.domain.restaurantCategory.repository.RestaurantCategoryRepository;
import Fodong.serverdong.domain.wishlist.Wishlist;
import Fodong.serverdong.domain.wishlist.repository.WishlistRepository;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;

    /**
     * 위시리스트 추가
     * @param restaurantId 식당 아이디
     * @param memberId 회원 아이디
     */
    @Transactional
    public void addWishlist(Long restaurantId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.MEMBER_NOT_FOUND));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.RESTAURANT_NOT_FOUND));

        // 해당 식당에 연결된 모든 카테고리를 조회
        List<RestaurantCategory> restaurantCategories = restaurantCategoryRepository.findByRestaurantId(restaurantId);

        List<Wishlist> wishlistsToAdd = new ArrayList<>();

        // 각 카테고리에 대해 위시리스트 생성
        for (RestaurantCategory restaurantCategory : restaurantCategories) {
            Category category = restaurantCategory.getCategory();

            // 이미 위시리스트에 동일한 항목이 있는지 검사
            Optional<Wishlist> existingWishlist = wishlistRepository.findByMemberAndRestaurantAndCategory(member, restaurant, category);
            if (existingWishlist.isPresent()) {
                continue;
            }

            Wishlist wishlist = Wishlist.builder()
                    .restaurant(restaurant)
                    .category(category)
                    .member(member)
                    .createdDate(LocalDateTime.now())
                    .build();
            wishlistsToAdd.add(wishlist);
        }
        wishlistRepository.saveAll(wishlistsToAdd);
    }

}
