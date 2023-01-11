package local.authorization.resource.server.service;

import local.authorization.resource.server.model.Brand;
import local.authorization.resource.server.model.Store;
import local.authorization.resource.server.model.ProductionType;

import java.util.List;

public interface StoreService {

    List<Store> getMobileStoresList();
    List<Store> getElectronicsStoresList();

    List<Store> getStoresListByProductionType(ProductionType productionType);

    List<Store> getStoresListByBrand(Brand brand);

    void setOrAddBrands(Long store, List<Long> brands);

}
