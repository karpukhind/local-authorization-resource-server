package local.authorization.resource.server.controller.rest;

import local.authorization.resource.server.dto.ProductDto;
import local.authorization.resource.server.repositories.ProductRepository;
import local.authorization.resource.server.model.Product;
import local.authorization.resource.server.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping(value = "/rest/products")
//@RequestMapping(value = "/rest")
public class ProductControllerRest {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private StoreRepository storeRepository;

	@RequestMapping(method = RequestMethod.POST, path = "/add")
	//@PostMapping("/")
	public String saveProduct(@RequestBody ProductDto productDto) {
		//		Product productPersisted = productRepository.save(new Product(product.getName(), product.getPrice()));
		Product product = new Product();
		product.setName(productDto.getName());
		product.setPrice(BigDecimal.valueOf(productDto.getPrice()));
		product.setStore(storeRepository.getById(productDto.getStoreId()));
		product.setDescription(productDto.getDescription());

		Product productPersisted = productRepository.save(product);
		return productPersisted.getId().toString();
	}

	@ResponseBody
	//@RequestMapping(method = RequestMethod.GET, path = "/list")
	@GetMapping("")
	public List<Product> getProducts() {
		return (List<Product>) productRepository.findAll();
	}

	@GetMapping("/{storeName}")
	public List<Product> getProducts(@PathVariable String storeName) {

		return productRepository.findProductsByStoreName(storeName);
	}

	@GetMapping("/store/{storeId}")
	public String setStore(@PathVariable String storeId, @RequestParam String productId) {

		int i = productRepository.updateProductWithStore(Long.parseLong(storeId), Long.parseLong(productId));
		return Integer.toString(i);
	}

}
