package com.reactive.programming;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class ReactiveProgrammingApplicationTest {

    ReactiveProgrammingApplication reactiveProgrammingApplication = new ReactiveProgrammingApplication();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void returnFluxTest() {
        var names = reactiveProgrammingApplication.returnFlux();

        StepVerifier.create(names)
                .expectNext("Alex")
                .expectNextCount(2)
                .verifyComplete();

    }

    @Test
    void returnFluxMAPTest() {
        var names = reactiveProgrammingApplication.returnFluxMap();

        StepVerifier.create(names)
                .expectNext("RANJITH")
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    void returnFluxFlatMAPTest() {
        var names = reactiveProgrammingApplication.returnFluxFlatMap();

        StepVerifier.create(names)
                .expectNext("R")
                .expectNextCount(14)
                .verifyComplete();

    }


    @Test
    void returnFluxFlatTransform() {
        var names = reactiveProgrammingApplication.returnFluxFlatMap();

        StepVerifier.create(names)
                .expectNext("R")
                .expectNextCount(14)
                .verifyComplete();

    }


    @Test
    void returnMonoFlatMAPTest() {
        var names = reactiveProgrammingApplication.returnFluxFlatMonop();

        StepVerifier.create(names)
                .expectNext(List.of("A","l","e","x")) ;

    }


    @Test
     void returnFluxDefaultIfEmptyTest(){

        var names = reactiveProgrammingApplication.returnFluxDefaultIfEmpty()   ;
        StepVerifier.create(names)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void returnFluxSwitchIfEmptyTest(){

        var names = reactiveProgrammingApplication.returnFluxDefaultIfEmpty()   ;
        StepVerifier.create(names)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void zipFluxTest(){

        var names = reactiveProgrammingApplication.zipFlux()   ;
        StepVerifier.create(names)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }




    @Test
    void concatFlux() {

        var  combinedFlux =  reactiveProgrammingApplication.concatFlux();
        StepVerifier.create(combinedFlux)
                .expectNext("Abc")
                .expectNext("Def")
                .verifyComplete();
    }
}