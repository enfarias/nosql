package com.treinamento.workshopcassandra.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treinamento.workshopcassandra.model.dto.ProductDTO;
import com.treinamento.workshopcassandra.model.entities.Product;
import com.treinamento.workshopcassandra.repositories.ProductRepository;
import com.treinamento.workshopcassandra.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	public ProductDTO findById(UUID id) {
		Product entity = getById(id);
		return new ProductDTO(entity);
	}
	
	private Product getById(UUID id) {
		Optional<Product> result = repository.findById(id);
		return result.orElseThrow(() -> new ResourceNotFoundException("Id não encontrado"));
	}
	
}
