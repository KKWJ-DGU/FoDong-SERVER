package Fodong.serverdong.domain.category;

import Fodong.serverdong.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Builder
@AllArgsConstructor
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name" , nullable = false)
    private String categoryName;

    @Column(name = "category_img_url" , nullable = false)
    private String categoryImgUrl;

}