package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.dto.ReviewsDTO;
import uz.pdp.kiyim_online_dokon.entity.Products;
import uz.pdp.kiyim_online_dokon.entity.Reviews;
import uz.pdp.kiyim_online_dokon.entity.Users;
import uz.pdp.kiyim_online_dokon.repository.ProductsRepository;
import uz.pdp.kiyim_online_dokon.repository.ReviewsRepository;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.ReviewsService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReviewsServiceImpl implements ReviewsService {
    private final ReviewsRepository reviewsRepository;
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;

    private ReviewsDTO toDTO(Reviews review) {
        return new ReviewsDTO(
                review.getId(),
                review.getUser().getId(),
                review.getProduct().getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }

    @Override
    public ReviewsDTO createReview(ReviewsDTO dto) {
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Products product = productsRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Reviews review = new Reviews();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Reviews saved = reviewsRepository.save(review);

        return toDTO(saved);
    }

    @Override
    public ReviewsDTO updateReview(Integer id, ReviewsDTO dto) {
        Reviews review = reviewsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(dto.getUserId())) {
            throw new RuntimeException("You cannot change review owner");
        }

        Products product = productsRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        review.setProduct(product);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Reviews updated = reviewsRepository.save(review);

        return toDTO(updated);
    }

    @Override
    public void deleteReview(Integer id) {
        Reviews review = reviewsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewsRepository.delete(review);
    }

    @Override
    public ReviewsDTO getReviewById(Integer id) {
        Reviews review = reviewsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        return toDTO(review);
    }

    @Override
    public List<ReviewsDTO> getAllReviews() {
        return reviewsRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<ReviewsDTO> getReviewsByProduct(Integer productId) {
        return reviewsRepository.findByProductId(productId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<ReviewsDTO> getReviewsByUser(Integer userId) {
        return reviewsRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
