package com.treinamento.workshopmongo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treinamento.workshopmongo.models.dto.UserDTO;
import com.treinamento.workshopmongo.models.entities.User;
import com.treinamento.workshopmongo.repositories.UserRepository;
import com.treinamento.workshopmongo.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public List<UserDTO> findAll() {
		List<User> list = repository.findAll();
		return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
	}

	public UserDTO findById(String id) {
		Optional<User> result = repository.findById(id);
		User entity = result.orElseThrow(() -> new ResourceNotFoundException("Objeto não encontrado"));
		return new UserDTO(entity);
	}
	
}
