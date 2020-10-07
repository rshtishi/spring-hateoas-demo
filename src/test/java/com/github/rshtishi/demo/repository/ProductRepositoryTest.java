package com.github.rshtishi.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.rshtishi.demo.entity.Product;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRepositoryTest {
	
	@Autowired
	private ProductRepository productRepository;

	@Test
	@Order(1)
	void testFindAll() {
		//execute
		List<Product> products = productRepository.findAll();
		//verify
		int expectedSize = 4;
		assertEquals(expectedSize, products.size());
	}
	
	@Test
	@Order(2)
	void testFindById() {
		//setup
		long id = 1;
		//execute
		Product product = productRepository.findById(id);
		//verify
		assertEquals(id, product.getId());
	}

}
