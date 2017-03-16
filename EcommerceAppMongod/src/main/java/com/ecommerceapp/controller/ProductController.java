package com.ecommerceapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecommerceapp.model.Product;
import com.ecommerceapp.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	ProductService productservice;
	
	
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String viewAddProduct(){
		return "add";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)		//1st
	public String addProduct(@ModelAttribute("addform") Product product){		//getting data from form
		productservice.addProduct(product);
		return "redirect:/";
	}
	
	@RequestMapping(value="/product",method=RequestMethod.GET,produces="application/json")
	@ResponseBody
	public List<Product> viewProducts(){
		return productservice.findAll();		//json	//2nd
	}
}
