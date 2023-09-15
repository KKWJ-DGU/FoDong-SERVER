package Fodong.serverdong.domain.restaurantCategory;

import Fodong.serverdong.domain.category.Category;
import Fodong.serverdong.domain.restaurant.Restaurant;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED,force = true)
@Getter
@Entity
public class RestaurantCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_category_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="category_id")
    private Category category;


}