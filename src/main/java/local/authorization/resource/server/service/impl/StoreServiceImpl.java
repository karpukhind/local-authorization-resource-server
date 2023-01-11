package local.authorization.resource.server.service.impl;

import com.google.common.collect.Lists;
import local.authorization.resource.server.model.Brand;
import local.authorization.resource.server.model.Store;
import local.authorization.resource.server.model.ProductionType;
import local.authorization.resource.server.repositories.BrandRepository;
import local.authorization.resource.server.repositories.StoreRepository;
import local.authorization.resource.server.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Store> getMobileStoresList() {
        return storeRepository.findByBrandsProductionType(ProductionType.MOBILE);
    }

    @Override
    public List<Store> getElectronicsStoresList() {
        return storeRepository.findByBrandsProductionType(ProductionType.ELECTRONICS);
    }

    @Override
    public List<Store> getStoresListByProductionType(ProductionType productionType) {
        return storeRepository.findByBrandsProductionType(productionType);
    }

    @Override
    public List<Store> getStoresListByBrand(Brand brand) {
        return null;
    }

    @Override
    public void setOrAddBrands(Long storeId, List<Long> brandIds) {
        Store store = storeRepository.getById(storeId);
        List<Brand> brands1 = Optional.ofNullable(store.getBrands()).orElseGet(() -> {
            store.setBrands(new ArrayList<>());
            return store.getBrands();
        });
        brands1.addAll(Lists.newArrayList(brandRepository.findAllById(brandIds)));
        storeRepository.saveAndFlush(store);
    }
}
