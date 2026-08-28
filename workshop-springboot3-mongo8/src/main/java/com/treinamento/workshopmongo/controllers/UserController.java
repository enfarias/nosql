package com.treinamento.workshopmongo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treinamento.workshopmongo.models.dto.UserDTO;
import com.treinamento.workshopmongo.services.UserService;

@RestController
@RequestMapping(value="/users")
public class UserController {

	@Autowired
	private UserService service;
	
	@GetMapping
 	public ResponseEntity<List<UserDTO>> findAll() {
		List<UserDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

}
