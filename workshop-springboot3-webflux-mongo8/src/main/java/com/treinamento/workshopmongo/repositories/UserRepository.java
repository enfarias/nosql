package com.treinamento.workshopmongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.treinamento.workshopmongo.models.entities.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

}
