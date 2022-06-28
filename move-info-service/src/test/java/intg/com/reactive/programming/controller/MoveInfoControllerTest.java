package com.reactive.programming.controller;

import com.reactive.programming.domain.MovieInfo;
import com.reactive.programming.repository.MovieInfoRepository;
import com.reactive.programming.service.MovieInfoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MoveInfoControllerTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setup(){
        var movieinfos = List.of(
                new MovieInfo("VIK2","Vikram","2022",List.of("Kamal","VJS","Fafa"), LocalDate.parse("2022-06-03")),
                new MovieInfo("KPK","KPK","2022",List.of("Nayan","VJS","SRP"), LocalDate.parse("2022-06-03")),
                new MovieInfo("DON","Don","2022",List.of("SK","SJS","PS"), LocalDate.parse("2022-06-03")));

        movieInfoRepository.saveAll(movieinfos).blockFirst();
    }

    @AfterEach
    void teardown(){

        movieInfoRepository.deleteAll().block();

    }
    @Test
    void addMovieTest(){

        MovieInfo movieInfo = new MovieInfo(null,"Beast","2022",
                List.of("JV","PH"), LocalDate.parse("2022-04-03"));

        webTestClient.post()
                .uri("/v1/addmovie")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieAdded -> {
                    assertNotNull(movieAdded.getResponseBody().getMovieInfoId());
                });


    }


    @Test
    void getAllMoviesTest(){


        webTestClient.get()
                .uri("/v1/movieinfos")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);



    }


    @Test
    void getAllMoviesById(){


        String movieId = "VIK2";

        webTestClient.get()
                .uri("/v1/movieById/{id}" , movieId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    assertNotNull(movieInfoEntityExchangeResult.getResponseBody().getMovieInfoId());
                });



    }


    @Test
    void updateMovieTest(){

        MovieInfo movieInfo =  new MovieInfo(null,"Vikram2","2022",List.of("Kamal","VJS","Fafa"), LocalDate.parse("2022-06-03"));

        webTestClient.put()
                .uri("/v1/update/{id}" , "VIK2")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieAdded -> {
                    assertEquals("Vikram2",movieAdded.getResponseBody().getName());
                });


    }

    @Test
    void delete(){

        var movieId ="Don";

        webTestClient.delete()
                .uri("/v1/delete/{id}" , movieId)
                .exchange()
                .expectStatus()
                .isNoContent();


    }


}