package Fodong.serverdong.domain.restaurant.dto.response;

import Fodong.serverdong.domain.category.dto.response.ResponseCategoryInfoListDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ResponseRestaurantAllInfoDto {

    private Long id;
    private String name;
    private String address;
    private String imgUrl;
    private String phoneNumber;
    private String webUrl;
    private List<ResponseCategoryInfoListDto> categoryInfoList;
    private List<ResponseMenuInfoListDto> menuInfoList;
    private int wishCount;
    private Boolean wishState;

    public ResponseRestaurantAllInfoDto(List<ResponseRestaurantBasicInfoDto> responseRestaurantBasicInfoDto,List<ResponseMenuInfoListDto> responseMenuInfoListDto, List<ResponseCategoryInfoListDto> responseCategoryInfoListDto){
        this.id= responseRestaurantBasicInfoDto.get(0).getId();
        this.name= responseRestaurantBasicInfoDto.get(0).getName();
        this.address= responseRestaurantBasicInfoDto.get(0).getAddress();
        this.imgUrl= responseRestaurantBasicInfoDto.get(0).getImgUrl();
        this.phoneNumber= responseRestaurantBasicInfoDto.get(0).getPhoneNumber();
        this.webUrl= responseRestaurantBasicInfoDto.get(0).getWebUrl();
        this.categoryInfoList= responseCategoryInfoListDto;
        this.menuInfoList= responseMenuInfoListDto;
        this.wishCount= responseRestaurantBasicInfoDto.get(0).getWishCount();
        this.wishState= responseRestaurantBasicInfoDto.get(0).getWishState();
    }

}
