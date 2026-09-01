package com.treinamento.workshopcassandra.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.treinamento.workshopcassandra.model.entities.Department;

public interface DepartmentRepository extends CassandraRepository<Department, UUID> {

}
