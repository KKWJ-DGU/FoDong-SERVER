package Fodong.serverdong.domain.restaurant.dto.response;
import lombok.*;

@AllArgsConstructor
@Getter
public class ResponseRestaurantDto {

    private String name;
    private String imgUrl;
    private String categoryName;
    private String menuName;
    private int wishCount;
    private Boolean wishState;



}
