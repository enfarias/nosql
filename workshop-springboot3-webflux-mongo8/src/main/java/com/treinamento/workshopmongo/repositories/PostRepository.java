package com.treinamento.workshopmongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.treinamento.workshopmongo.models.entities.Post;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {

}
