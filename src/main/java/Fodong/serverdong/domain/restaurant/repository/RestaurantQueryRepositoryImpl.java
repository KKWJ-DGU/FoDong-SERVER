package Fodong.serverdong.domain.restaurant.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantInfoDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static Fodong.serverdong.domain.menu.QMenu.menu;
import static Fodong.serverdong.domain.restaurant.QRestaurant.restaurant;
import static Fodong.serverdong.domain.restaurantCategory.QRestaurantCategory.restaurantCategory;
import static Fodong.serverdong.domain.wishlist.QWishlist.wishlist;
import static com.querydsl.jpa.JPAExpressions.*;

@Repository
public class RestaurantQueryRepositoryImpl implements RestaurantQueryRepository{

    private final JPAQueryFactory query;

    public RestaurantQueryRepositoryImpl(EntityManager em){
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 랜덤 식당 리스트 조회
     */
    @Override
    public List<ResponseRestaurantDto> getRandomRestaurant(){

        return query.
                select(Projections.constructor(
                        ResponseRestaurantDto.class,
                        restaurant.name,
                        restaurant.imgUrl,
                        select(Expressions.stringTemplate("group_concat({0})",restaurantCategory.category.categoryName))
                                .from(restaurantCategory)
                                .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                        select(Expressions.stringTemplate("group_concat({0})",menu.menuName))
                                .from(menu)
                                .where(menu.restaurant.id.eq(restaurant.id)),
                        restaurant.wishCount,
                        restaurant.id.in(
                                JPAExpressions.select(restaurant.id)
                                        .from(restaurant)
                                        .leftJoin(wishlist).on(restaurant.id.eq(wishlist.restaurant.id))
                                        .where(wishlist.member.id.eq(1L))
                        )
                ))
                .from(restaurant)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .fetch();

    }

    /**
     * 카테고리 별 식당 리스트 조회
     */
    @Override
    public List<ResponseRestaurantDto> getRestaurant(List<Long> categoryId){
        return query
                .select(Projections.constructor(
                        ResponseRestaurantDto.class,
                        restaurant.name,
                        restaurant.imgUrl,
                        select(Expressions.stringTemplate("group_concat({0})",restaurantCategory.category.categoryName))
                                .from(restaurantCategory)
                                .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                        select(Expressions.stringTemplate("group_concat({0})",menu.menuName))
                                .from(menu)
                                .where(menu.restaurant.id.eq(restaurant.id)),
                        restaurant.wishCount,
                        restaurant.id.in(
                                JPAExpressions.select(restaurant.id)
                                        .from(restaurant)
                                        .leftJoin(wishlist).on(restaurant.id.eq(wishlist.restaurant.id))
                                        .where(wishlist.member.id.eq(1L))
                        )

                ))
                .from(restaurant)
                .leftJoin(restaurantCategory).on(restaurant.id.eq(restaurantCategory.restaurant.id))
                .where(restaurantCategory.category.id.in(categoryId))
                .distinct()
                .fetch();
    }

    /**
     * 식당 정보 조회
     * @param restaurantId 식당 ID
     */
    @Override
    public ResponseRestaurantInfoDto getRestaurantInfo(Long restaurantId){
        return query.select(Projections.constructor(
                ResponseRestaurantInfoDto.class,
                restaurant.name,
                restaurant.imgUrl,
                select(Expressions.stringTemplate("group_concat({0})",restaurantCategory.category.categoryName))
                        .from(restaurantCategory)
                        .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                restaurant.phoneNumber,
                restaurant.webUrl,
                select(Expressions.stringTemplate("group_concat({0})",menu.menuName))
                        .from(menu)
                        .where(menu.restaurant.id.eq(restaurant.id)),
                restaurant.wishCount,
                restaurant.id.in(
                        JPAExpressions.select(restaurant.id)
                                .from(restaurant)
                                .leftJoin(wishlist).on(restaurant.id.eq(wishlist.restaurant.id))
                                .where(wishlist.member.id.eq(1L))
                )))
                .from(restaurant)
                .where(restaurant.id.eq(restaurantId))
                .fetchOne();

    }

    /**
     * 랜덤 식당 1개 조회
     */
    @Override
    public ResponseRestaurantDto getRandomRestaurantChoice(){
        return query
                .select(Projections.constructor(
                        ResponseRestaurantDto.class,
                        restaurant.name,
                        restaurant.imgUrl,
                        select(Expressions.stringTemplate("group_concat({0})",restaurantCategory.category.categoryName).coalesce("카테고리 없음").as("categoryName"))
                                .from(restaurantCategory)
                                .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                        select(Expressions.stringTemplate("group_concat({0})",menu.menuName).coalesce("메뉴 없음").as("menuName"))
                                .from(menu)
                                .where(menu.restaurant.id.eq(restaurant.id)),
                        restaurant.wishCount,
                        restaurant.id.in(
                                JPAExpressions.select(restaurant.id)
                                        .from(restaurant)
                                        .leftJoin(wishlist).on(restaurant.id.eq(wishlist.restaurant.id))
                                        .where(wishlist.member.id.eq(1L))
                        )

                ))
                .from(restaurant)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .fetchFirst();
    }
}
