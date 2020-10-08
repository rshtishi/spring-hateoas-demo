# spring-hateoas-demo

A practical guide that demostrate how to implement Spring HATEOAS in the simpliest way possible. 

## Topics

Hypermedia as the Engine of Application State, or HATEOAS, is a means of creating self-describing APIs wherein resources returned from an API contain links to related
resources. By using HATEOAS clients can navigate an API with minimal understanding of the API’s URLs. In this demo we shall see how to implement HATEOAS with Spring Boot:

- define the domain model
- accessing data from the database with spring data
- implementing the controller
- implementing HATEOAS

### Define the domain model

The heart of every application is its domain model. In this simple example, we have only one entity that will be used to store the application state. We are using 
Lombok for removing the boilerplate code when implementing the POJO. Below is the domain class:

```
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

	@Id
	private long id;
	private String name;
	private double price;
	private String manufacturer;

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
```

### Accessing data from the database with spring data 

We are using spring data JDBC to access the database because it has all the features of JPA implemented in a much more lightweight and simple manner. Below is we have
declared the repository for accessing the database:

```
public interface ProductRepository extends CrudRepository<Product, Long> {
	
	public List<Product> findAll();
	
	public Product findById(long id);
	
}

```

### Implementing the controller

We have created a simple rest controller that has one dependency that is the JDBC repository. Below is the controller class:

```
@RestController
@RequestMapping("/products")
public class ProductRestController {

	@Autowired
	private ProductRepository productRepository;
  
  	@GetMapping
	public ResponseEntity<ListProduct>> findAll() {
      ....
  }
  
}
  
```

### Implementing HATEOAS

As we said in the beginning, HATEOAS is self-describing REST endpoints. We need to add additional information for navigating in API. Spring HATEOAS defines a generic 
EntityModel<T> container hat lets us decorate any domain object with additional links. CollectionModel is another class of Spring HATEOAS that let us decorate
a collection of domain objects. Below is an example:


```
	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<Product>>> findAll() {
		
		List<EntityModel<Product>> content = productRepository.findAll().stream()
				.map(product ->{
					Link selfLink = linkTo(methodOn(ProductRestController.class).findById(product.getId())).withSelfRel();
					Link productsLink =linkTo(methodOn(ProductRestController.class).findAll()).withRel("productsLink");
					return EntityModel.of(product,selfLink,productsLink);
				})
				.collect(Collectors.toList());
		Link selfLink = linkTo(methodOn(ProductRestController.class).findAll()).withSelfRel();
		CollectionModel<EntityModel<Product>> productCollectionModel = CollectionModel.of(content, selfLink);
		return ResponseEntity.ok(productCollectionModel);
	}
```
