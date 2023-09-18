package Fodong.serverdong.domain.restaurant.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseRandomRestaurantDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.criterion.Projection;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;

import static Fodong.serverdong.domain.restaurant.QRestaurant.restaurant;

@Repository
public class RestaurantQueryRepositoryImpl implements RestaurantQueryRepository{

    private final JPAQueryFactory query;

    public RestaurantQueryRepositoryImpl(EntityManager em){
        this.query=new JPAQueryFactory(em);
    }
    @Override
    public List<ResponseRandomRestaurantDto> getRandomRestaurant(){
        return query
                .select(Projections.constructor(
                        ResponseRandomRestaurantDto.class,
                        restaurant.name,
                        restaurant.imgUrl,
                        restaurant.wishCount
                ))
                .from(restaurant)
                .orderBy(NumberExpression.random().desc())
                .fetch();
    }
}
