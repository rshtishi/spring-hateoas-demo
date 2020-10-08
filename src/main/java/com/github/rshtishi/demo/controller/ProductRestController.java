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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
				.map(product -> EntityModel.of(product,
						linkTo(methodOn(ProductRestController.class).findById(product.getId())).withSelfRel(),
						linkTo(methodOn(ProductRestController.class).findAll()).withSelfRel()))
				.collect(Collectors.toList());
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
			return ResponseEntity
					.ok(optionalProduct.map(product -> EntityModel.of(product, selfLink, productsLink)).get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<EntityModel<Product>> create(@RequestBody Product product) {
		product = productRepository.save(product);
		EntityModel<Product> productEntity = EntityModel.of(product);
		Link selfLink = linkTo(methodOn(ProductRestController.class).findById(product.getId())).withSelfRel();
		Link viewAllLink = linkTo(methodOn(ProductRestController.class).findAll()).withRel("viewAllLink");
		productEntity.add(selfLink, viewAllLink);
		return ResponseEntity.ok(productEntity);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Product>> update(@PathVariable("id") Long id, @RequestBody Product product) {
		product.setId(id);
		product = productRepository.save(product);
		EntityModel<Product> productEntity = EntityModel.of(product);
		Link selfLink = linkTo(methodOn(ProductRestController.class).findById(product.getId())).withSelfRel();
		Link viewAllLink = linkTo(methodOn(ProductRestController.class).findAll()).withRel("viewAllLink");
		productEntity.add(selfLink, viewAllLink);
		return ResponseEntity.ok(productEntity);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<EntityModel<Object>> delete(@PathVariable("id") Long id) {
		productRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
