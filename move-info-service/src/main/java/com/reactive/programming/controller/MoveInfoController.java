package com.reactive.programming.controller;


import com.reactive.programming.domain.MovieInfo;
import com.reactive.programming.service.MovieInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class MoveInfoController {

    MovieInfoService movieInfoService;

    public MoveInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @PostMapping("/addmovie")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody  MovieInfo movieInfo){

         return  movieInfoService.saveMovieInfo(movieInfo).log();

    }

    @GetMapping("/movieinfos")
    public Flux<MovieInfo> getAllMovies(){

        return  movieInfoService.getAllMovies().log();

    }

    @GetMapping("/movieById/{id}")
    public  Mono<MovieInfo> getMovieInfoById(@PathVariable String id){

        return  movieInfoService.getMovieInfoByID(id).log();

    }

    @PutMapping("/update/{id}")
    public  Mono<MovieInfo> update(@RequestBody MovieInfo movieInfo,
                                       @PathVariable String id){

        return  movieInfoService.updateMovie(movieInfo,id).log();

    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  Mono<Void> delete(@PathVariable String id){

        return movieInfoService.deleteMovie(id).log();
    }
}
