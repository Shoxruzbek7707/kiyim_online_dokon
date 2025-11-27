package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewsDTO {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
