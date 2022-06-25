package com.reactive.programming.Router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewRoute(){

        return  route()
                .GET("/v1/helloworld",(request -> ServerResponse.ok().bodyValue("Hello World")))
                .build();


    }
}
