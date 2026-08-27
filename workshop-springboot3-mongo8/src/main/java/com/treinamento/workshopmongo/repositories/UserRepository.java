package com.treinamento.workshopmongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.treinamento.workshopmongo.models.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
