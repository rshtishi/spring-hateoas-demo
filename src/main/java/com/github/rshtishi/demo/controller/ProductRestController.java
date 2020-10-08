package com.github.rshtishi.demo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		Link selfLink = linkTo(methodOn(ProductRestController.class).findAll()).withSelfRel();
		CollectionModel<EntityModel<Product>> productCollectionModel = CollectionModel.of(content, selfLink);
		return ResponseEntity.ok(productCollectionModel);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Product>> findById(@PathVariable("id") Long id) {

		Optional<Product> optionalProduct = productRepository.findById(id);
		if (optionalProduct.isPresent()) {
			Link selfLink = linkTo(methodOn(ProductRestController.class).findById(id)).withSelfRel();
			Link productsLink = linkTo(methodOn(ProductRestController.class).findAll()).withRel("products");
			return ResponseEntity.ok(optionalProduct.map(product -> EntityModel.of(product, selfLink,productsLink)).get());
		}
		return ResponseEntity.notFound().build();
	}

}
