package Fodong.serverdong.domain.restaurantCategory.repository;

import Fodong.serverdong.domain.restaurantCategory.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {
    List<RestaurantCategory> findByRestaurantId(Long restaurantId);
}
