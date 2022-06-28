package com.reactive.programming.Handler;

import com.reactive.programming.domain.Review;
import com.reactive.programming.repository.ReviewInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ReviewHandler {

    private final ReviewInfoRepository reviewInfoRepository;

    public ReviewHandler(ReviewInfoRepository reviewInfoRepository) {
        this.reviewInfoRepository = reviewInfoRepository;
    }

    public Mono<ServerResponse> addReview(ServerRequest request) {

        return request.bodyToMono(Review.class)
                .flatMap(reviewInfoRepository::save)
                .flatMap(savedReview -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedReview));
    }

    public Mono<ServerResponse> getAllReviews(ServerRequest request) {

        var reviewsFlux = reviewInfoRepository.findAll();
        return ServerResponse.ok().body(reviewsFlux,Review.class);
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {

        var reviewId = request.pathVariable("id");

        var existingReview = reviewInfoRepository.findById(Long.parseLong(reviewId));

        return existingReview.flatMap(review -> request.bodyToMono(Review.class)
                .map(requestReview -> {
                        review.setRating(requestReview.getRating());
                        review.setComment(requestReview.getComment());
                        return  review;
                })
                .flatMap(reviewInfoRepository::save)
                .flatMap(savedReview -> ServerResponse.ok().bodyValue(savedReview)));
    }

    private Review updateReview(Review existingReview, Review revisedReview) {
        existingReview.setComment(revisedReview.getComment());
        existingReview.setRating(revisedReview.getRating());
        return existingReview;
    }

}
