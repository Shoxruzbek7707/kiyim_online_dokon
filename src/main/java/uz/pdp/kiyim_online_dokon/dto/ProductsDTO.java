package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsDTO {

    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Integer categoryId;
    private String categoryName;
    private LocalDateTime createdAt;

    /**
     * ✅ Endi asosiy rasmning URL'ini saqlaydi
     */
    private String mainImageUrl;

    private List<Integer> imageIds;
}