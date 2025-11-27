package uz.pdp.kiyim_online_dokon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.ReviewsDTO;
import uz.pdp.kiyim_online_dokon.service.interfaces.ReviewsService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewsController {
    private final ReviewsService reviewsService;

    @PostMapping
    public ResponseEntity<ReviewsDTO> createReview(@RequestBody ReviewsDTO reviewsDTO) {
        ReviewsDTO reviewsDTO1 = reviewsService.createReview(reviewsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewsDTO1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewsDTO>  updateReview(@PathVariable Integer id, @RequestBody ReviewsDTO reviewsDTO) {
        ReviewsDTO reviewsDTO1 = reviewsService.updateReview(id, reviewsDTO);
        return ResponseEntity.ok(reviewsDTO1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteReview(@PathVariable Integer id) {
        reviewsService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewsDTO> getReviewById(@PathVariable Integer id) {
        ReviewsDTO reviewsDTO1 = reviewsService.getReviewById(id);
        return ResponseEntity.ok(reviewsDTO1);
    }

    @GetMapping
    public ResponseEntity<List<ReviewsDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewsService.getAllReviews());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewsDTO>> getReviewsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(reviewsService.getReviewsByUser(userId));
    }
}
