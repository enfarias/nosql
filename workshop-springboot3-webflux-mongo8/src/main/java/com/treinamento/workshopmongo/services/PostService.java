package com.treinamento.workshopmongo.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treinamento.workshopmongo.dto.PostDTO;
import com.treinamento.workshopmongo.repositories.PostRepository;
import com.treinamento.workshopmongo.services.exceptions.ResourceNotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {

	@Autowired
	private PostRepository repository;

	public Mono<PostDTO> findById(String id) {
		return repository.findById(id)
				.map(PostDTO::new)
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Recurso não encontrado")));
	}

	public Flux<PostDTO> findByTitle(String text) {
		return repository.searchTitle(text)
				.map(PostDTO::new);
	}	

	public Flux<PostDTO> fullSearch(String text, Instant startMoment, Instant endMoment) {
		endMoment = (endMoment != null) ? endMoment.plusSeconds(86400) : null;
		return repository.fullSearch(text, startMoment, endMoment)
				.map(PostDTO::new);
	}
	
	public Flux<PostDTO> findByUser(String id) {
		return repository.findByUser(id)
				.map(PostDTO::new);
	}
}