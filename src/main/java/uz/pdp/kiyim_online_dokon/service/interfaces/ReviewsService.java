package uz.pdp.kiyim_online_dokon.service.interfaces;

import uz.pdp.kiyim_online_dokon.dto.ReviewsDTO;

import java.util.List;

public interface ReviewsService {
    ReviewsDTO createReview(ReviewsDTO dto);

    ReviewsDTO updateReview(Integer id, ReviewsDTO dto);

    void deleteReview(Integer id);

    ReviewsDTO getReviewById(Integer id);

    List<ReviewsDTO> getAllReviews();

    List<ReviewsDTO> getReviewsByProduct(Integer productId);

    List<ReviewsDTO> getReviewsByUser(Integer userId);
}
