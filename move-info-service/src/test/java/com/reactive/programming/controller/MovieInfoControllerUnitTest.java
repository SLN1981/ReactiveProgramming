package com.reactive.programming.controller;


import com.reactive.programming.domain.MovieInfo;
import com.reactive.programming.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MoveInfoController.class)
@AutoConfigureWebTestClient
public class MovieInfoControllerUnitTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    MovieInfoService movieInfoService;

    @Test
    void getAllMoviesTest(){


        var movieinfos = List.of(
                new MovieInfo("VIK2","Vikram","2022",List.of("Kamal","VJS","Fafa"), LocalDate.parse("2022-06-03")),
                new MovieInfo("KPK","KPK","2022",List.of("Nayan","VJS","SRP"), LocalDate.parse("2022-06-03")),
                new MovieInfo("DON","Don","2022",List.of("SK","SJS","PS"), LocalDate.parse("2022-06-03")));

        when(movieInfoService.getAllMovies()).thenReturn(Flux.fromIterable(movieinfos));

        webTestClient.get()
                .uri("/v1/movieinfos")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);



    }


    @Test
    void getMovieInfoByIDTest(){
        var movieinfo = Mono.just(new MovieInfo("VIK2","Vikram","2022",List.of("Kamal","VJS","Fafa"),
                        LocalDate.parse("2022-06-03")));

        var movieId = "VIK2";

        when(movieInfoService.getMovieInfoByID(movieId)).thenReturn(movieinfo);

        webTestClient.get()
                .uri("/v1/movieById/{id}" , movieId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var movieInfoName =movieInfoEntityExchangeResult.getResponseBody().getName();
                    assertEquals(movieInfoName,"Vikram");
                });
    }

    @Test
    void addMovieTest(){

        MovieInfo movieInfo = new MovieInfo("ABC","Beast","2022",
                List.of("JV","PH"), LocalDate.parse("2022-04-03"));

        Mono<MovieInfo> movieInfoMono = Mono.just(movieInfo);

        when(movieInfoService.saveMovieInfo(movieInfo)).thenReturn(movieInfoMono);

        webTestClient.post()
                .uri("/v1/addmovie")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieAdded -> {
                    assertNotNull(movieAdded.getResponseBody().getMovieInfoId());
                    assertEquals("ABC",movieAdded.getResponseBody().getMovieInfoId());
                });


    }


    @Test
    void updateMovieTest(){

        MovieInfo movieInfo =  new MovieInfo("VIk2","Vikram2","2022",List.of("Kamal","VJS","Fafa"), LocalDate.parse("2022-06-03"));

        Mono<MovieInfo> movieInfoMono = Mono.just(movieInfo);

        when(movieInfoService.updateMovie(movieInfo,"VIK2")).thenReturn(movieInfoMono);

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
    void deleteMovieInfoById(){


        var movieId ="Don";


   when(movieInfoService.deleteMovie(movieId)).thenReturn(Mono.empty());


        webTestClient.delete()
                .uri("/v1/delete/{id}" , movieId)
                .exchange()
                .expectStatus()
                .isNoContent();

    }




}
