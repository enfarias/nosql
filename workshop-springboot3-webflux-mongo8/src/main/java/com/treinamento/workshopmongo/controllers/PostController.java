package com.treinamento.workshopmongo.controllers;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.treinamento.workshopmongo.controllers.util.URL;
import com.treinamento.workshopmongo.dto.PostDTO;
import com.treinamento.workshopmongo.services.PostService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

	@Autowired
	private PostService service;

	@GetMapping(value = "/{id}")
	public Mono<ResponseEntity<PostDTO>> findById(@PathVariable String id) {
		return service.findById(id)
				.map(ResponseEntity::ok);
	}

	@GetMapping(value = "/titlesearch")
	public Flux<PostDTO> findByTitle(@RequestParam(defaultValue = "") String text) {
		return service.findByTitle(text);
	}
	
	@GetMapping(value = "/fullsearch")
	public Flux<PostDTO> fullSearch(
			@RequestParam(defaultValue = "") String text,
			@RequestParam(defaultValue = "") String start,
			@RequestParam(defaultValue = "") String end) {

		Instant min = URL.convertDate(start, Instant.EPOCH);
		Instant max = URL.convertDate(end, Instant.now());

		return service.fullSearch(text, min, max);
	}	
}