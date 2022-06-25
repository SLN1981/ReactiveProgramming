package com.reactive.programming.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest(controllers = FluxMonoController.class)
@AutoConfigureWebTestClient
class FluxMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void flux(){
        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(3);
    }


    @Test
    void flux_approach2(){
       var flux =  webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class)
               .getResponseBody();

        StepVerifier.create(flux)
                .expectNext(1,2,3)
                .verifyComplete();
    }



    @Test
    void monoTest(){
        var mono =  webTestClient.get()
                .uri("/mono")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                        .consumeWith( stringEntityExchangeResult -> {
                            var responseBody = stringEntityExchangeResult.getResponseBody();
                            assertEquals("Hello World",responseBody);
                        });

    }

    @Test
    void stream(){
        var flux =  webTestClient.get()
                .uri("/stream")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(flux)
                .expectNext(0L, 1L,2L,3L)
                .thenCancel()//Stop the Stream
                .verify();
    }

}