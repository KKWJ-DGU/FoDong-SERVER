package Fodong.serverdong.domain.wishlist.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
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
    public List<ResponseRestaurantDto> getWishlistRestaurant(Long memberId, Long categoryId) {
        return query
                .select(Projections.constructor(
                        ResponseRestaurantDto.class,
                        restaurant.name,
                        restaurant.imgUrl,
                        select(Expressions.stringTemplate("group_concat({0})", restaurantCategory.category.categoryName))
                                .from(restaurantCategory)
                                .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                        select(Expressions.stringTemplate("group_concat({0})", menu.menuName))
                                .from(menu)
                                .where(menu.restaurant.id.eq(restaurant.id)),
                        restaurant.wishCount,
                        Expressions.constant(true)
                ))
                .from(restaurant)
                .join(wishlist).on(restaurant.id.eq(wishlist.restaurant.id))
                .leftJoin(restaurantCategory).on(restaurant.id.eq(restaurantCategory.restaurant.id))
                .where(
                        wishlist.member.id.eq(memberId),
                        eqCategoryId(categoryId)
                )
                .distinct()
                .fetch();
    }
    private BooleanExpression eqCategoryId(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return restaurantCategory.category.id.eq(categoryId);
    }


}

