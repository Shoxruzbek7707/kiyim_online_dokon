package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {

    private Integer id;

    /**
     * ✅ Endi byte[] emas, rasmga kirish uchun URL/Path qaytariladi
     */
    private String imageUrl;

    private Boolean isMain;
    private Integer productId;
}