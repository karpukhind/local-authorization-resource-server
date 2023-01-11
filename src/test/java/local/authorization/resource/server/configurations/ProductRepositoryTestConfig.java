package local.authorization.resource.server.configurations;

import local.authorization.resource.server.model.Brand;
import local.authorization.resource.server.model.Product;
import local.authorization.resource.server.repositories.ProductRepository;
import local.authorization.resource.server.service.ProductService;
import local.authorization.resource.server.service.impl.ProductServiceImpl;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static local.authorization.resource.server.TestConstants.STORE_NAME_1;
import static local.authorization.resource.server.TestConstants.STORE_NAME_1;

@Configuration
public class ProductRepositoryTestConfig {

    @MockBean
    private ProductRepository productRepository;

    @PostConstruct
    public void init() {

        System.out.println("ProductRepositoryTestConfig init");
        when(productRepository.findProductsByStoreName(STORE_NAME_1)).thenReturn(getProduct());

    }

    @Bean
    public ProductService productService() {
        return new ProductServiceImpl();
    }

    private List<Product> getProduct() {

        Brand sony = new Brand();
        sony.setName("sony");
        sony.setCountry("Japan");

        Product product1 = new Product();
        product1.setName("Bravia 55");
        product1.setDescription("qwerty Brawia flatscreen TV 55 inch qwerty");
        product1.setBrand(sony);

        Product product2 = new Product();
        product2.setName("Bravia 65");
        product2.setDescription("qwerty Brawia flatscreen TV 65 inch qwerty");
        product2.setBrand(sony);

        Product product3 = new Product();
        product3.setName("Bravia 75");
        product3.setDescription("qwerty Brawia flatscreen TV 75 inch qwerty");
        product3.setBrand(sony);


        Product product4 = new Product();
        product4.setName("Playstation");
        product4.setDescription("qwerty PlayStation5 qwerty");
        product3.setBrand(sony);

        //sony.setProducts(Arrays.asList(product1, product2, product3, product4));

        return Arrays.asList(product1, product2, product3, product4);
    }




}
