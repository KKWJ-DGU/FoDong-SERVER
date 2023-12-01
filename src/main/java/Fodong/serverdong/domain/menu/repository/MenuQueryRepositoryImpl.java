package Fodong.serverdong.domain.menu.repository;

import Fodong.serverdong.domain.restaurant.dto.response.ResponseMenuInfoListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static Fodong.serverdong.domain.menu.QMenu.menu;

@Repository
public class MenuQueryRepositoryImpl implements MenuQueryRepository {

    private final JPAQueryFactory query;

    public MenuQueryRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<ResponseMenuInfoListDto> getMenuInfoList(Long restaurantId){
        return query.select(Projections.constructor(
                ResponseMenuInfoListDto.class,
                menu.menuName,
                menu.menuPrice
        ))
                .from(menu)
                .where(menu.restaurant.id.eq(restaurantId))
                .fetch();
    }

}
