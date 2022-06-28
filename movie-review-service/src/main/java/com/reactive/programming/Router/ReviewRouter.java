package com.reactive.programming.Router;

import com.reactive.programming.Handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewRoute(ReviewHandler reviewHandler){

        return  route()
                .GET("/v1/helloworld",(request -> ServerResponse.ok().bodyValue("Hello World")))
                .POST("/v1/reviews",request -> reviewHandler.addReview(request))
                .GET("/v1/getAllReviews",request -> reviewHandler.getAllReviews(request).log())
                .PUT("/v1/updateReviews/{id}", request -> reviewHandler.updateReview(request))
                .build();


    }
}
