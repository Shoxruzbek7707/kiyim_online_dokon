package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductImageDTO {
    private Integer id;
    private Integer productId;
    private Boolean isMain;
    private byte[] imageBytes;

    public ProductImageDTO() {

    }
}
