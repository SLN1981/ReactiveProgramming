package com.reactive.programming.repository;

import com.reactive.programming.domain.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReviewInfoRepository extends ReactiveMongoRepository<Review,Long> {
}
