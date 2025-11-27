package uz.pdp.kiyim_online_dokon.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Integer id;
    private Integer userId;

    private String region;
    private String district;
    private String street;
    private String house;
    private String apartment;

    private Boolean isDefault;
}
