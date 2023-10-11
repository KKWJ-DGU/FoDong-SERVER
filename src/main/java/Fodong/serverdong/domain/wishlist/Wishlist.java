package Fodong.serverdong.domain.wishlist;

import Fodong.serverdong.domain.BaseTimeEntity;
import Fodong.serverdong.domain.category.Category;
import Fodong.serverdong.domain.member.Member;
import Fodong.serverdong.domain.restaurant.Restaurant;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Builder
@AllArgsConstructor
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JoinColumn(name = "created_date" , nullable = false)
    private LocalDateTime createdDate;

}
