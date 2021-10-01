package com.example.demo.repository;

import com.example.demo.domain.Person;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository {

	Person andy = new Person(1, "Andy", "Wang");
	Person sally = new Person(2, "Sally", "Wang");
	Person chiying = new Person(3, "Chiying", "Wang");
	Person peter = new Person(4, "Peter", "Wang");

	@Override
	public Mono<Person> getById(Integer id) {
		return Mono.just(andy);
	}

	@Override
	public Flux<Person> findAll() {
		return Flux.just(andy, sally, chiying, peter);
	}

}