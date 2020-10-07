package com.github.rshtishi.demo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<CollectionModel<EntityModel<Product>>> findAll() {

		List<EntityModel<Product>> content = productRepository.findAll().parallelStream()
				.map(product -> EntityModel.of(product)).collect(Collectors.toList());

		Link link = linkTo(methodOn(ProductRestController.class).findAll()).withSelfRel();

		CollectionModel<EntityModel<Product>> productCollectionModel = CollectionModel.of(content, link);

		return ResponseEntity.ok(productCollectionModel);
	}

}
