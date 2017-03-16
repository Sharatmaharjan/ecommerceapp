package com.ecommerceapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerceapp.model.Product;
import com.ecommerceapp.repo.ProductRepo;

@Service
public class ProductService {

	@Autowired
	ProductRepo productrepo;
	
	public void addProduct(Product product){
		productrepo.save(product);
	}
	
	public Product findById(String id){
		return productrepo.findById(id);
	}
	
	public List<Product> findAll(){
		return productrepo.findAll();
	}
	
	public List<Product> findByName(String name){
		List<Product> lis=productrepo.findByName(name);		//reduce to one line code
		return lis;
	}
	
	public List<Product> findByCategory(String category){
		List<Product> lis=productrepo.findByCategory(category);
		return lis;
	}
	
	public List<Product> findByPrice(String price){	//for sorting findByLessThanPrice or morethanprice or between two prices
		List<Product> lis=productrepo.findByPrice(price);
		return lis;
	}
//	public List<Product> findByType(String type){
//		List<Product> lis=productrepo.findByType(type);
//		return lis;
//	}
	
	
}
