package com.github.rshtishi.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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

}
