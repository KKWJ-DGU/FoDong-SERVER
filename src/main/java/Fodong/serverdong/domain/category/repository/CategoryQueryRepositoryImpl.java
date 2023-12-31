package Fodong.serverdong.domain.category.repository;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryInfoListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static Fodong.serverdong.domain.category.QCategory.category;

@Repository
public class CategoryQueryRepositoryImpl implements CategoryQueryRepository {

    private final JPAQueryFactory query;

    public CategoryQueryRepositoryImpl(EntityManager em){
        this.query = new JPAQueryFactory(em);
    }
    @Override
    public List<ResponseCategoryInfoListDto> getCategoryList(){
        return query.select(Projections.constructor(
                ResponseCategoryInfoListDto.class,
                category.id,
                category.categoryName,
                category.categoryImgUrl
        ))
                .from(category)
                .fetch();
    }


}
