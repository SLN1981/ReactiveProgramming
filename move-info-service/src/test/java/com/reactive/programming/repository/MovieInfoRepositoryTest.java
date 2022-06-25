package com.reactive.programming.repository;

import com.reactive.programming.domain.MovieInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setup(){
        var movieinfos = List.of(
                new MovieInfo("VIK2","Vikram","2022",List.of("Kamal","VJS","Fafa"), LocalDate.parse("2022-06-03")),
                new MovieInfo("KPK","KPK","2022",List.of("Nayan","VJS","SRP"), LocalDate.parse("2022-06-03")),
                new MovieInfo("DON","Don","2022",List.of("SK","SJS","PS"), LocalDate.parse("2022-06-03")));

        movieInfoRepository.saveAll(movieinfos).blockFirst();
    }

    @Test
    void findAll(){
        var movieInfo = movieInfoRepository.findAll().log();
        StepVerifier.create(movieInfo)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById(){
        var movieInfo = movieInfoRepository.findById("VIK2").log();
        StepVerifier.create(movieInfo)
                .assertNext( movie -> assertEquals("Vikram",movie.getName()))
                .verifyComplete();
    }


    @Test
    void save(){

        var movie96 =   new MovieInfo(null,"96","2022",List.of("VJS","TK"), LocalDate.parse("2019-06-03"));
        var movieInfo = movieInfoRepository.save(movie96).log();
        StepVerifier.create(movieInfo)
                .assertNext( movie -> assertNotNull(movie.getMovieInfoId()))
                .verifyComplete();
    }

    @Test
    void update(){
        var movie96 = movieInfoRepository.findById("DON").block();
          movie96.setYear("2023");
         var movieinfoMono =  movieInfoRepository.save(movie96).log();

        StepVerifier.create(movieinfoMono)
                .assertNext( movie -> assertEquals("Don",movie.getName()))
                .verifyComplete();



    }




}