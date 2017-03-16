package com.ecommerceapp.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecommerceapp.model.Product;

@Repository
public interface ProductRepo extends MongoRepository<Product, String> {

	Product findById(String id);

	List<Product> findByName(String name);
	
	List<Product> findByCategory(String category);

	List<Product> findByPrice(String price);

}
