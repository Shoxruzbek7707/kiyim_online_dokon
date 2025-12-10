package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductsDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Integer categoryId;
    private List<Integer> imageIds;
    private LocalDateTime createdAt;

    public ProductsDTO() {

    }
}
