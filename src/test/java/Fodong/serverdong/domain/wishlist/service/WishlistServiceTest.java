package Fodong.serverdong.domain.wishlist.service;

import Fodong.serverdong.domain.category.repository.CategoryRepository;
import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.member.repository.MemberRepository;
import Fodong.serverdong.domain.restaurant.Restaurant;
import Fodong.serverdong.domain.category.Category;
import Fodong.serverdong.domain.restaurant.repository.RestaurantRepository;
import Fodong.serverdong.domain.restaurantCategory.RestaurantCategory;
import Fodong.serverdong.domain.restaurantCategory.repository.RestaurantCategoryRepository;
import Fodong.serverdong.domain.wishlist.Wishlist;
import Fodong.serverdong.domain.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class WishlistServiceTest {


    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WishlistRepository wishlistRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RestaurantCategoryRepository restaurantCategoryRepository;
    @Autowired
    WishlistService wishlistService;

    Long testMemberId;
    Long testRestaurantId;

    @Test
    @DisplayName("위시리스트 추가")
    void addWishlistTest(){

        Member testMember = Member.builder()
                .email("testEmail@exmaple.com")
                .nickname("Test Nickname")
                .build();
        memberRepository.save(testMember);
        testMemberId = memberRepository.save(testMember).getId();

        Restaurant testRestaurant = Restaurant.builder()
                .id(1L)
                .name("세븐일레븐")
                .webUrl("webUrl")
                .phoneNumber("031-000-0000")
                .address("경기도")
                .imgUrl("imgUrl")
                .wishCount(0)
                .build();
        restaurantRepository.save(testRestaurant);
        testRestaurantId = restaurantRepository.save(testRestaurant).getId();

        Category testCategory = Category.builder()
                .id(1L)
                .categoryName("TestCategory")
                .categoryImgUrl("testImageUrl")
                .build();
        categoryRepository.save(testCategory);

        RestaurantCategory testRestaurantCategory = RestaurantCategory.builder()
                .id(1L)
                .restaurant(testRestaurant)
                .category(testCategory)
                .build();
        restaurantCategoryRepository.save(testRestaurantCategory);

        wishlistService.addWishlist(testRestaurantId, testMemberId);

        List<Wishlist> wishlists = wishlistRepository.findByMember(testMember);
        assertThat(wishlists).isNotEmpty();
        for (Wishlist wishlist : wishlists) {
            assertThat(wishlist.getMember().getId()).isEqualTo(testMemberId);
            assertThat(wishlist.getRestaurant().getId()).isEqualTo(testRestaurantId);
        }

    }
}