package com.treinamento.workshopmongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treinamento.workshopmongo.dto.UserDTO;
import com.treinamento.workshopmongo.models.entities.User;
import com.treinamento.workshopmongo.repositories.UserRepository;
import com.treinamento.workshopmongo.services.exceptions.ResourceNotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public Flux<UserDTO> findAll() {
		return repository.findAll()
				.map(UserDTO::new);
	}

	public Mono<UserDTO> findById(String id) {
		return repository.findById(id)
				.map(UserDTO::new)
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Recurso não encontrado")));
	}

	public Mono<UserDTO> insert(UserDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		return repository.save(entity)
				.map(UserDTO::new);
	}	
	
	public Mono<UserDTO> update(String id, UserDTO dto) {
	    return repository.findById(id)
	            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Recurso não encontrado")))
	            .doOnNext(entity -> copyDtoToEntity(dto, entity))
	            .flatMap(repository::save)
	            .map(UserDTO::new);
	}	
	
	
	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
	}
	
}