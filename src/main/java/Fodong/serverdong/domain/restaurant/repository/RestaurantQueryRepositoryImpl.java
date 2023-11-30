package Fodong.serverdong.domain.restaurant.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseMenuInfo;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseRestaurantInfoDto;
import Fodong.serverdong.domain.restaurant.dto.response.ResponseSearchRestaurantDto;
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
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
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
    public List<ResponseRestaurantDto> getRandomRestaurant(Long memberId){

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
                                JPAExpressions.select(restaurantCategory.restaurant.id)
                                        .from(restaurantCategory)
                                        .leftJoin(wishlist).on(restaurantCategory.id.eq(wishlist.restaurantCategory.id))
                                        .where(wishlist.member.id.eq(memberId))
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
    public List<ResponseRestaurantDto> getRestaurant(Long categoryId, Long memberId){
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
                                JPAExpressions.select(restaurantCategory.restaurant.id)
                                        .from(restaurantCategory)
                                        .leftJoin(wishlist).on(restaurantCategory.id.eq(wishlist.restaurantCategory.id))
                                        .where(wishlist.member.id.eq(memberId))
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
    public List<ResponseRestaurantInfoDto> getRestaurantInfo(Long restaurantId,Long memberId){

        return query.selectFrom(restaurant)
                .leftJoin(menu).on(restaurant.id.eq(menu.restaurant.id))
                .where(restaurant.id.eq(restaurantId))
                .transform(groupBy(restaurant.id).list(
                        Projections.constructor(
                                ResponseRestaurantInfoDto.class,
                                restaurant.name,
                                restaurant.address,
                                restaurant.imgUrl,
                                select(Expressions.stringTemplate("group_concat({0})",restaurantCategory.category.categoryName))
                                        .from(restaurantCategory)
                                        .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                                restaurant.phoneNumber,
                                restaurant.webUrl,
                                list(Projections.constructor(
                                        ResponseMenuInfo.class,
                                        menu.menuName,
                                        menu.menuPrice
                                )),
                                restaurant.wishCount,
                                restaurant.id.in(
                                        JPAExpressions.select(restaurantCategory.restaurant.id)
                                                .from(restaurantCategory)
                                                .leftJoin(wishlist).on(restaurantCategory.id.eq(wishlist.restaurantCategory.id))
                                                .where(wishlist.member.id.eq(memberId))
                                ))));

    }

    /**
     * 랜덤 식당 1개 조회
     */
    @Override
    public List<ResponseRestaurantDto> getRandomRestaurantChoice(Long memberId){
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
                                JPAExpressions.select(restaurantCategory.restaurant.id)
                                        .from(restaurantCategory)
                                        .leftJoin(wishlist).on(restaurantCategory.id.eq(wishlist.restaurantCategory.id))
                                        .where(wishlist.member.id.eq(memberId))
                        )

                ))
                .from(restaurant)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(15L)
                .fetch();
    }

    /**
     * 검색 식당 조회
     */
    @Override
    public List<ResponseSearchRestaurantDto> getSearchRestaurant(List<Long> categoryId,Long memberId){

        return query.select(
                        Projections.constructor(
                                ResponseSearchRestaurantDto.class,
                                restaurant.name,
                                restaurant.imgUrl,
                                select(Expressions.stringTemplate("group_concat({0})",restaurantCategory.category.id))
                                        .from(restaurantCategory)
                                        .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                                select(Expressions.stringTemplate("group_concat({0})",restaurantCategory.category.categoryName))
                                        .from(restaurantCategory)
                                        .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                                select(Expressions.stringTemplate("group_concat({0})",menu.menuName))
                                        .from(menu)
                                        .where(menu.restaurant.id.eq(restaurant.id)),
                                restaurant.wishCount,
                                restaurant.id.in(
                                        JPAExpressions.select(restaurantCategory.restaurant.id)
                                                .from(restaurantCategory)
                                                .leftJoin(wishlist).on(restaurantCategory.id.eq(wishlist.restaurantCategory.id))
                                                .where(wishlist.member.id.eq(memberId))
                                )
                        ))
                .from(restaurantCategory)
                .join(restaurantCategory.restaurant,restaurant)
                .where(restaurantCategory.category.id.in(categoryId))
                .distinct()
                .fetch();


    }

}
