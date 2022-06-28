package com.reactive.programming.route;


import com.reactive.programming.domain.Review;
import com.reactive.programming.repository.ReviewInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class ReviewRouterTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReviewInfoRepository reviewInfoRepository;

    final String REVIEW_SAVE_URL = "/v1/reviews";

    final String GET_ALL_REVIEWS_URL = "/v1/getAllReviews";

    final String UPDATE_REVIEW = "/v1/updateReviews/{id}";

    @BeforeEach
    void setUp() {

        var reviewsList = List.of(
                new Review("1", 1L, "Awesome Movie", 9.0),
                new Review("2", 1L, "Awesome Movie1", 9.0),
                new Review("3", 2L, "Excellent Movie", 8.0));
        reviewInfoRepository.saveAll(reviewsList).log()
                .blockLast();

    }

    @AfterEach
    void tearDown() {
        reviewInfoRepository.deleteAll().block();
    }

    @Test
    void saveReviewTest() {

        var review =  new Review(null, 1L, "Awesome Movie", 9.0);

        webTestClient.post()
                .uri(REVIEW_SAVE_URL)
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    var savedReview = reviewEntityExchangeResult.getResponseBody().getReviewId();
                    assertNotNull(savedReview);
                });
    }

    @Test
    void getAllMovies() {
        webTestClient.get()
                .uri(GET_ALL_REVIEWS_URL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Review.class)
                .hasSize(3)
                .consumeWith(moviesList -> {
                    var moviesInfo = moviesList.getResponseBody().get(0).getReviewId();
                    assertEquals("1",moviesInfo);
                });
    }

    @Test
    void updatedReview() {


        var  review  = new Review(null, 1L, "Epic Movie", 10.0);
        webTestClient.put()
                .uri(UPDATE_REVIEW,"1")
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    var updatedReview = reviewEntityExchangeResult.getResponseBody();
                    assertEquals("Epic Movie",updatedReview.getComment());
                    assertEquals(10.0,updatedReview.getRating());

                });


    }
}
