package Fodong.serverdong.domain.restaurant;

import Fodong.serverdong.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED,force = true)
@Getter
@Entity
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "web_url" , nullable = false)
    private String webUrl;

    @Column(name = "phone_number" , nullable = false)
    private String phoneNumber;

    @Column(name = "address" , nullable = false)
    private String address;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "wish_count" , columnDefinition = "INT DEFAULT 0")
    private int wishCount;

    public void decreaseWishCount() {
        this.wishCount = Math.max(0, this.wishCount - 1);
    }

}