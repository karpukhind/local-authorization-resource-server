package local.authorization.resource.server.service.impl;

import local.authorization.resource.server.configurations.ProductRepositoryTestConfig;
import local.authorization.resource.server.configurations.StoreRepositoryTestConfig;
import local.authorization.resource.server.model.Brand;
import local.authorization.resource.server.model.Product;
import local.authorization.resource.server.repositories.ProductRepository;
import local.authorization.resource.server.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;

import static local.authorization.resource.server.TestConstants.STORE_NAME_1;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextHierarchy(
        {
                @ContextConfiguration(classes = StoreRepositoryTestConfig.class),
                @ContextConfiguration(classes = ProductRepositoryTestConfig.class)
        }
)
@ActiveProfiles("test")
public class ProductServiceImplTest {


    @Autowired
    private ProductService productService;

    @Test
    public void checkFlatTVsInStock() {
        List<Product> flatTvInStock = productService.getFlatTvInStock();
        assertEquals(3, flatTvInStock.size());
        assertEquals("sony", flatTvInStock.get(1).getBrand().getName());
    }

}
