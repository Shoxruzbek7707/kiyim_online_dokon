package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDTO {
    private Integer id;
    private String name;

    public CategoryDTO() {

    }
}
