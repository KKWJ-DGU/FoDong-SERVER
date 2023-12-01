package Fodong.serverdong.domain.wishlist.repository;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryInfoListDto;
import Fodong.serverdong.domain.wishlist.dto.response.ResponseWishlistRestaurantDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static Fodong.serverdong.domain.menu.QMenu.menu;
import static Fodong.serverdong.domain.restaurant.QRestaurant.restaurant;
import static Fodong.serverdong.domain.restaurantCategory.QRestaurantCategory.restaurantCategory;
import static Fodong.serverdong.domain.wishlist.QWishlist.wishlist;

@Repository
public class WishlistQueryRepositoryImpl implements WishlistQueryRepository {

    private final JPAQueryFactory query;

    public WishlistQueryRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 위시리스트 식당 리스트 조회
     */
    @Override
    public List<ResponseWishlistRestaurantDto> getWishlistRestaurant(Long memberId, List<Long> categoryId) {
        return query
                .select(Projections.constructor(
                        ResponseWishlistRestaurantDto.class,
                        restaurant.id,
                        wishlist.id,
                        restaurant.name,
                        restaurant.imgUrl,
                        select(Expressions.stringTemplate("group_concat({0})", restaurantCategory.category.id))
                                .from(restaurantCategory)
                                .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                        select(Expressions.stringTemplate("group_concat({0})", restaurantCategory.category.categoryName))
                                .from(restaurantCategory)
                                .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                        select(Expressions.stringTemplate("group_concat({0})", menu.menuName))
                                .from(menu)
                                .where(menu.restaurant.id.eq(restaurant.id)),
                        restaurant.wishCount,
                        Expressions.constant(true)
                ))
                .from(wishlist)
                .join(wishlist.restaurantCategory, restaurantCategory)
                .join(restaurantCategory.restaurant, restaurant)
                .where(
                        wishlist.member.id.eq(memberId),
                        eqCategoryId(categoryId)
                )
                .orderBy(wishlist.id.desc())
                .distinct()
                .fetch();
    }
    private BooleanExpression eqCategoryId(List<Long> categoryId) {
        if (categoryId.contains(0L)) {
            return null;
        }
        return restaurantCategory.category.id.in(categoryId);
    }


    /**
     * 위시리스트 카테고리 리스트 조회
     */
    @Override
    public List<ResponseCategoryInfoListDto> getWishlistCategory(Long memberId) {
        return query.select(Projections.constructor(
                        ResponseCategoryInfoListDto.class,
                        wishlist.restaurantCategory.category.id,
                        wishlist.restaurantCategory.category.categoryName,
                        wishlist.restaurantCategory.category.categoryImgUrl
                ))
                .from(wishlist)
                .where(wishlist.member.id.eq(memberId))
                .distinct()
                .fetch();
    }


}

