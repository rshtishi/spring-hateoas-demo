package com.github.rshtishi.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rshtishi.demo.entity.Product;
import com.github.rshtishi.demo.repository.ProductRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductRepository productRepository;

	@Test
	void testFindAll() throws Exception {
		// setup
		List<Product> products = Arrays.asList(new Product(1L, "Lap Top", 1000, "China"));
		when(productRepository.findAll()).thenReturn(products);
		// execute
		MockHttpServletResponse response = mvc.perform(get("/products")).andReturn().getResponse();
		// verify
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).contains("_embedded");
		assertThat(response.getContentAsString()).contains("productList");
		assertThat(response.getContentAsString()).contains("_links");
	}

	@Test
	void testFindById() throws Exception {
		// setup
		Long id = 1L;
		Product product = new Product(1L, "Lap Top", 1000, "China");
		when(productRepository.findById(id)).thenReturn(Optional.of(product));
		// execute
		MockHttpServletResponse response = mvc.perform(get("/products/1")).andReturn().getResponse();
		// verify
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).contains("id");
		assertThat(response.getContentAsString()).contains("_links");
	}

	@Test
	void testCreate() throws Exception {
		// setup
		Product product = new Product(1L, "Lap Top", 1000, "China");
		when(productRepository.save(product)).thenReturn(product);
		String payload = new ObjectMapper().writeValueAsString(product);
		// execute
		MockHttpServletResponse response = mvc
				.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(payload)).andReturn()
				.getResponse();
		// verify
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).contains("id");
		assertThat(response.getContentAsString()).contains("_links");
	}
	
	@Test
	void testUpdate() throws Exception {
		// setup
		Long id = 1L;
		Product product = new Product(1L, "Lap Top", 1000, "China");
		when(productRepository.save(product)).thenReturn(product);
		String payload = new ObjectMapper().writeValueAsString(product);
		// execute
		MockHttpServletResponse response = mvc
				.perform(put("/products/1").contentType(MediaType.APPLICATION_JSON).content(payload)).andReturn()
				.getResponse();
		// verify
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).contains("id");
		assertThat(response.getContentAsString()).contains("_links");
	}
	
	@Test
	void testDelete() throws Exception {
		//setup
		Long id = 1L;
		doNothing().when(productRepository).deleteById(id);
		//execute
		MockHttpServletResponse response = mvc
				.perform(delete("/products/1")).andReturn()
				.getResponse();
		//verify
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
		verify(productRepository).deleteById(id);
	}

}
