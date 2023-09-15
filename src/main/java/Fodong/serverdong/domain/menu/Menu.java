package Fodong.serverdong.domain.menu;

import Fodong.serverdong.domain.BaseTimeEntity;
import Fodong.serverdong.domain.restaurant.Restaurant;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED,force = true)
@Getter
@Entity
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "menu_name" , nullable = false)
    private String menuName;

    @Column(name = "menu_price" , nullable = false)
    private String menuPrice;



}