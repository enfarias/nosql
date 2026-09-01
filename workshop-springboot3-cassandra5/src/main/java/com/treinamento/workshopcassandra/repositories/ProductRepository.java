package com.treinamento.workshopcassandra.repositories;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.treinamento.workshopcassandra.model.entities.Product;

public interface ProductRepository extends CassandraRepository<Product, UUID> {

}
