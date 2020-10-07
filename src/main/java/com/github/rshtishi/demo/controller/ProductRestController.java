package com.github.rshtishi.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rshtishi.demo.entity.Product;
import com.github.rshtishi.demo.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductRestController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping
	public List<Product> findAllProducts(){
		return productRepository.findAll();
	}

}
