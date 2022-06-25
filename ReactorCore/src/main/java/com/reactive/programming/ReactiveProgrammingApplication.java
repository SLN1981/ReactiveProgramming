package com.reactive.programming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@SpringBootApplication
public class ReactiveProgrammingApplication {

	public Flux<String> returnFlux(){
		return Flux.fromIterable(List.of("Alex","Ranjith","Fernando")).log();
	}

	public Mono<String> returnMono(){
		return  Mono.just("Vetri").log();
	}


	public Flux<String> returnFluxMap(){
		return Flux.fromIterable(List.of("Alex","Ranjith","Fernando"))
				.map(String::toUpperCase)
				.filter(s -> s.length() > 4)
				.log();

	}


	public Flux<String> returnFluxFlatMap(){

		Function<Flux<String>,Flux<String>> filtermap = name -> name.map(String::toUpperCase);
		return Flux.fromIterable(List.of("Alex","Ranjith","Fernando"))
				.transform(filtermap)
				.filter(s -> s.length() > 4)
				.flatMap(this::splitNames)
				.log();

	}

	public Flux<String> returnFluxDefaultIfEmpty(){

		Function<Flux<String>,Flux<String>> filtermap = name -> name.map(String::toUpperCase);
		return Flux.fromIterable(List.of("Alex","Ranjith","Fernando"))
				.transform(filtermap)
				.filter(s -> s.length() > 15)
				.defaultIfEmpty("default")
				.log();

	}

	public Flux<String> returnFluxSwitchIfEmpty(){

		Function<Flux<String>,Flux<String>> filtermap = name -> name.map(String::toUpperCase);
		Flux<String> namesFlux  = Flux.just("default");
		return Flux.fromIterable(List.of("Alex","Ranjith","Fernando"))
				.transform(filtermap)
				.filter(s -> s.length() > 15)
				.switchIfEmpty(namesFlux)
				.log();

	}

	public  Flux<String> concatFlux(){

		var abcFlux = Flux.just("Abc");
		var defFlux = Flux.just("Def");

		var concatFlux = Flux.concat(abcFlux,defFlux).log();

		return concatFlux;
	}


	public  Flux<String> mergeFlux(){

		var abcFlux = Flux.just("Abc");
		var defFlux = Flux.just("Def");

		var concatFlux = Flux.merge(abcFlux,defFlux).log();

		return concatFlux;
	}



	public  Flux<String> zipFlux(){

		var abcFlux = Flux.just("A","B","C");
		var defFlux = Flux.just("D","E","F");



		return Flux.zip(abcFlux,defFlux,(first,second) -> first+second).log();
	}

	public Flux<String> returnFluxTransform(){
		return Flux.fromIterable(List.of("Alex","Ranjith","Fernando"))
				.map(String::toUpperCase)
				.filter(s -> s.length() > 4)
				.log();

	}

	public Mono<List<String>> returnFluxFlatMonop(){
		return Mono.just("Alex")
				.filter(s -> s.length() > 4)
				.flatMap(this::splitNamesForMono)
				.log();

	}

	public  Flux<String> splitNames(String names)
	{
		var nameArray = names.split("");
		return Flux.fromArray(nameArray);
	}

	public  Mono<List<String>> splitNamesForMono(String names)
	{
		var nameArray = names.split("");
		var charArray = List.of(nameArray);
		return Mono.just(charArray);
	}


	public static void main(String[] args) {
		SpringApplication.run(ReactiveProgrammingApplication.class, args);
		ReactiveProgrammingApplication reactiveProgrammingApplication = new ReactiveProgrammingApplication();
		reactiveProgrammingApplication.returnFlux().subscribe(p -> System.out.println(p));
		reactiveProgrammingApplication.returnMono().subscribe(p -> System.out.println(p));
	}

}
