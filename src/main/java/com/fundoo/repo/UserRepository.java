package com.fundoo.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.fundoo.model.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String>{

	Mono<User> findByEmail(String email);

}
