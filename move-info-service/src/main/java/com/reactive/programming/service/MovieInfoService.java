package com.reactive.programming.service;


import com.reactive.programming.domain.MovieInfo;
import com.reactive.programming.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

    MovieInfoRepository moveInfoRepository;

    public MovieInfoService(MovieInfoRepository moveInfoRepository) {
        this.moveInfoRepository = moveInfoRepository;
    }

    public Mono<MovieInfo> saveMovieInfo(MovieInfo movieInfo){
       return moveInfoRepository.save(movieInfo);
    }

    public Flux<MovieInfo> getAllMovies(){return  moveInfoRepository.findAll();}

    public Mono<MovieInfo> getMovieInfoByID(String id){return  moveInfoRepository.findById(id);}

    public Mono<MovieInfo> updateMovie(MovieInfo movieInfo, String id) {

        return moveInfoRepository.findById(id)
                .flatMap(movieInfo1 -> {
                    movieInfo1.setYear(movieInfo.getYear());
                    movieInfo1.setName(movieInfo.getName());
                    return moveInfoRepository.save(movieInfo);
                });
    }

    public Mono<Void> deleteMovie(String id) {
        return moveInfoRepository.deleteById(id);
    }
}


