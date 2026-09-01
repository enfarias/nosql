package com.treinamento.workshopcassandra.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.treinamento.workshopcassandra.model.dto.DepartmentDTO;
import com.treinamento.workshopcassandra.model.entities.Department;
import com.treinamento.workshopcassandra.repositories.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository repository;
	
	public List<DepartmentDTO> findAll() {
		List<Department> list = repository.findAll();
		return list.stream().map(x -> new DepartmentDTO(x)).collect(Collectors.toList());
	}
		
}
