package local.authorization.resource.server.service.impl;


import local.authorization.resource.server.TestConstants;
import local.authorization.resource.server.configurations.StoreRepositoryTestConfig;
import local.authorization.resource.server.model.ProductionType;
import local.authorization.resource.server.model.Store;
import local.authorization.resource.server.repositories.StoreRepository;
import local.authorization.resource.server.service.StoreService;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextHierarchy(
    {
        @ContextConfiguration(classes = StoreRepositoryTestConfig.class)
    }
)
@ActiveProfiles("test")
public class StoreServiceImplTest {

    @Autowired
    private StoreService storeService;

    @Test
    public void getStoresForBrandsAndProductionType() {
        List<Store> stores = storeService.getStoresListByProductionType(ProductionType.APPAREL);
        assertEquals(1, stores.size());
        Store store = stores.get(0);
        assertEquals(1, store.getBrands().size());

        assertEquals(TestConstants.STORE_NAME_APPAREL, store.getName());
        assertEquals(TestConstants.BRAND_NAME_APPAREL, store.getBrands().get(0).getName());

    }



}
