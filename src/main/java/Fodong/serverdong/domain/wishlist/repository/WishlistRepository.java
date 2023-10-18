package Fodong.serverdong.domain.wishlist.repository;

import Fodong.serverdong.domain.category.Category;
import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.restaurant.Restaurant;
import Fodong.serverdong.domain.wishlist.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByMember(Member member);
    Optional<Wishlist> findByMemberAndRestaurantAndCategory(Member member, Restaurant restaurant, Category category);
    List<Wishlist> findByMemberAndRestaurant(Member member, Restaurant restaurant);

}
