package com.ecommerceapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecommerceapp.model.Product;
import com.ecommerceapp.service.ProductService;

@Controller
public class CategoryController {

	@Autowired
	ProductService productservice;
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String homePage(){
		System.out.println("hello from homepage");
		return "greeting";		//page
	}
	
	@RequestMapping(value="/category",method=RequestMethod.GET)
	public String viewCategory(){
		return "category";	//page
	}
	
	@RequestMapping(value="/electronics",method=RequestMethod.GET,produces="application/json")
	public List<Product> viewProductsByCategoryElectronics(){
		return productservice.findByCategory("electronics");	//ignorecasesensitive	//json
	}
	
	@RequestMapping(value="/bikes",method=RequestMethod.GET,produces="application/json")
	public List<Product> viewProductsByCategoryBikes(){
		return productservice.findByCategory("bikes");		//json
	}
	
	@RequestMapping(value="/cars",method=RequestMethod.GET,produces="application/json")
	public List<Product> viewProductsByCategoryCars(){
		return productservice.findByCategory("cars");		//json
	}
}
