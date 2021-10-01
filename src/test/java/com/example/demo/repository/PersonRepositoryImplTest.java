package com.example.demo.repository;

import com.example.demo.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new PersonRepositoryImpl();
    }

    @Test
    void getById() {
        Mono<Person> personMono = repository.getById(1);

        Person person = personMono.block();

        System.out.println(person.toString());
    }

    @Test
    void getByIdSubscribe() {
        Mono<Person> personMono = repository.getById(1);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void getByIdSubscribeNotFound() {
        Mono<Person> personMono = repository.getById(11);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void getByIdMapFunction() {
        Mono<Person> personMono = repository.getById(1);

        personMono.map(person -> {
            System.out.println(person.toString());
            return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println("from map: " + firstName);
        });
    }

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> personFlux = repository.findAll();

        Person person = personFlux.blockFirst();

        System.out.println(person.toString());
    }

    @Test
    void getAllFluxTest() {
        Flux<Person> personFlux = repository.findAll();

        StepVerifier.create(personFlux).expectNextCount(5).verifyComplete();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void getAllFluxTestMapFunction() {
        Flux<Person> personFlux = repository.findAll();

        personFlux.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxToListMono() {
        Flux<Person> personFlux = repository.findAll();

        Mono<List<Person>> personMono = personFlux.collectList();

        personMono.subscribe(list -> {
            list.forEach(person -> {
                System.out.println(person.toString());
            });
        });
    }

    @Test
    void testFindPersonById() {
        Flux<Person> personFlux = repository.findAll();

        final Integer id = 3;

        Mono<Person> personMono = personFlux.filter( p -> p.getId() == id).next();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = repository.findAll();

        final Integer id = 10;

        Mono<Person> personMono = personFlux.filter( p -> p.getId() == id).next();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFindPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = repository.findAll();

        final Integer id = 10;

        Mono<Person> personMono = personFlux.filter( p -> p.getId() == id).single();

        personMono.doOnError(throwable -> {
            System.out.println("Something goes wrong!!");
        }).onErrorReturn(Person.builder().id(id).build()).subscribe(person -> {
            System.out.println(person.toString());
        });
    }

}