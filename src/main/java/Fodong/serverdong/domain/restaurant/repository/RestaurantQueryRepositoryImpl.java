package Fodong.serverdong.domain.restaurant.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRandomRestaurantDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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

    @Override
    public List<ResponseRandomRestaurantDto> getRandomRestaurant(){

        return query.
                select(Projections.constructor(
                        ResponseRandomRestaurantDto.class,
                        restaurant.name,
                        restaurant.imgUrl,
                        select(Expressions.stringTemplate("group_concat({0})",restaurantCategory.category.categoryName))
                                .from(restaurantCategory)
                                .where(restaurantCategory.restaurant.id.eq(restaurant.id)),
                        select(Expressions.stringTemplate("group_concat({0})",menu.menuName))
                                .from(menu)
                                .where(menu.restaurant.id.eq(restaurant.id)),
                        restaurant.wishCount,
                        wishlist.member.id.isNotNull()
                ))
                .from(restaurant)
                .leftJoin(wishlist).on(restaurant.id.eq(wishlist.restaurant.id))
                .orderBy(NumberExpression.random().desc())
                .groupBy(restaurant.name)
                .fetch();

    }

}
