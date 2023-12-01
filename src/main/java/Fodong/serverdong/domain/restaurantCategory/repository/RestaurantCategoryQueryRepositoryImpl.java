package Fodong.serverdong.domain.restaurantCategory.repository;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryInfoListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static Fodong.serverdong.domain.restaurantCategory.QRestaurantCategory.restaurantCategory;

@Repository
public class RestaurantCategoryQueryRepositoryImpl implements RestaurantCategoryQueryRepository {

    private final JPAQueryFactory query;

    public RestaurantCategoryQueryRepositoryImpl(EntityManager em) {this.query = new JPAQueryFactory(em);}

    @Override
    public List<ResponseCategoryInfoListDto> getRestaurantCategoryId(Long restaurantId){

        return query.select(Projections.constructor(
                ResponseCategoryInfoListDto.class,
                restaurantCategory.category.id,
                restaurantCategory.category.categoryName,
                restaurantCategory.category.categoryImgUrl
        ))
                .from(restaurantCategory)
                .where(restaurantCategory.restaurant.id.eq(restaurantId))
                .fetch();
    }
}
