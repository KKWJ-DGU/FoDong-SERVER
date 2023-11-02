package Fodong.serverdong.domain.wishlist.service;

import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.restaurant.Restaurant;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.repository.RestaurantRepository;
import Fodong.serverdong.domain.restaurantCategory.RestaurantCategory;
import Fodong.serverdong.domain.restaurantCategory.repository.RestaurantCategoryRepository;
import Fodong.serverdong.domain.wishlist.Wishlist;
import Fodong.serverdong.domain.wishlist.dto.request.RequestWishlistCreationDto;
import Fodong.serverdong.domain.wishlist.repository.WishlistQueryRepository;
import Fodong.serverdong.domain.wishlist.repository.WishlistRepository;
import Fodong.serverdong.global.exception.CustomErrorCode;
import Fodong.serverdong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final WishlistQueryRepository wishlistQueryRepository;

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

        // 이미 위시리스트에 추가되어 있는지 검사
        boolean isAlreadyWishlisted = !wishlistRepository.findByMemberAndRestaurantCategoryIn(member, restaurantCategories).isEmpty();

        // 각 카테고리에 대해 위시리스트 생성
        for (RestaurantCategory restaurantCategory : restaurantCategories) {

            // 이미 위시리스트에 동일한 항목이 있는지 검사
            Optional<Wishlist> existingWishlist = wishlistRepository.findByMemberAndRestaurantCategory(member, restaurantCategory);
            if (existingWishlist.isPresent()) {
                continue;
            }

            Wishlist wishlist = RequestWishlistCreationDto.toEntity(member, restaurantCategory);
            wishlistsToAdd.add(wishlist);
        }
        wishlistRepository.saveAll(wishlistsToAdd);

        // wishCount 증가
        if (!isAlreadyWishlisted) {
            restaurant.increaseWishCount();
            restaurantRepository.save(restaurant);
        }
    }

    /**
     * 위시리스트 삭제
     * @param restaurantId 식당 아이디
     * @param memberId 회원 아이디
     */
    @Transactional
    public void deleteWishlist(Long restaurantId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.MEMBER_NOT_FOUND));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.RESTAURANT_NOT_FOUND));

        List<RestaurantCategory> restaurantCategories = restaurantCategoryRepository.findByRestaurantId(restaurantId);
        List<Wishlist> wishlistsToDelete = wishlistRepository.findByMemberAndRestaurantCategoryIn(member, restaurantCategories);

        if (wishlistsToDelete.isEmpty()) {
            // 삭제할 위시리스트가 없으면 바로 종료
            return;
        }

        wishlistRepository.deleteAll(wishlistsToDelete);

        restaurant.decreaseWishCount();
        restaurantRepository.save(restaurant);

    }

    /**
     * 위시리스트 조회
     * @param memberId 회원 아이디
     * @param categoryId 카테고리 아이디
     */
    @Transactional
    public Map<String,List<ResponseRestaurantDto>> getWishlist(Long memberId, Long categoryId) {
        return Collections.singletonMap("wishlist",wishlistQueryRepository.getWishlistRestaurant(memberId, categoryId));
    }


}
