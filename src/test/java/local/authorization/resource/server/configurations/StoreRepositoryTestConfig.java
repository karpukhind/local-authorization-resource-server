package local.authorization.resource.server.configurations;

import local.authorization.resource.server.model.Brand;
import local.authorization.resource.server.model.Store;
import local.authorization.resource.server.model.ProductionType;
import local.authorization.resource.server.repositories.BrandRepository;
import local.authorization.resource.server.repositories.StoreRepository;
import local.authorization.resource.server.service.StoreService;
import local.authorization.resource.server.service.impl.StoreServiceImpl;
import org.mockito.Mockito;

import static local.authorization.resource.server.TestConstants.BRAND_NAME_APPAREL;
import static local.authorization.resource.server.TestConstants.BRAND_NAME_SONY;
import static local.authorization.resource.server.TestConstants.STORE_NAME_APPAREL;
import static local.authorization.resource.server.TestConstants.STORE_NAME_1;
import static org.mockito.Mockito.when;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class StoreRepositoryTestConfig {

    @PostConstruct
    public void init() {
        System.out.println("StoreRepositoryTestConfig init");
    }

    @Bean
    public StoreService storeService() {
        return new StoreServiceImpl();
    }
    @Bean
    public BrandRepository brandRepository() {
        BrandRepository brandRepository = Mockito.mock(BrandRepository.class);
        //when(brandRepository.findProductsByStoreName(STORE_NAME_1)).thenReturn(getProduct());
        return brandRepository;
    }

    @Bean
    public StoreRepository storeRepository() {
        StoreRepository storeRepository = Mockito.mock(StoreRepository.class);
        when(storeRepository.findByBrandsProductionType(ProductionType.ELECTRONICS))
                .thenReturn(getStoreTest(STORE_NAME_1, BRAND_NAME_SONY, ProductionType.ELECTRONICS));
        when(storeRepository.findByBrandsProductionType(ProductionType.APPAREL))
                .thenReturn(getStoreTest(STORE_NAME_APPAREL, BRAND_NAME_APPAREL, ProductionType.APPAREL));

        return storeRepository;
    }

    private List<Store> getStoreTest(String storeName, String brandName, ProductionType productionType) {
        Store store = new Store();
        store.setName(storeName);

        Brand brand = new Brand();
        brand.setName(brandName);
        brand.setProductionType(productionType);
        store.setBrands(Collections.singletonList(brand));

        return Arrays.asList(store);
    }


}
