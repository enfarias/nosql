package com.treinamento.workshopmongo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treinamento.workshopmongo.models.dto.PostDTO;
import com.treinamento.workshopmongo.services.PostService;

@RestController
@RequestMapping(value="/posts")
public class PostController {

	@Autowired
	private PostService service;

	@GetMapping(value="/{id}")
 	public ResponseEntity<PostDTO> findById(@PathVariable String id) {
		PostDTO obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
}
