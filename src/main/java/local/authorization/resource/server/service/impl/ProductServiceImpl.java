package local.authorization.resource.server.service.impl;

import local.authorization.resource.server.model.Product;
import local.authorization.resource.server.model.Store;
import local.authorization.resource.server.repositories.ProductRepository;
import local.authorization.resource.server.service.ProductService;
import local.authorization.resource.server.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StoreService storeService;

    @Override
    public List<Product> getFlatTvInStock() {
        List<Store> electronicsStores = storeService.getElectronicsStoresList();
        return electronicsStores.stream()
                .map(Store::getName)
                .map(productRepository::findProductsByStoreName)
                .flatMap(List::stream)
                .filter(product -> product.getDescription().toLowerCase().matches(".*flatscreen\\s+tv\\s+\\d+\\s+inch.*"))
                .collect(Collectors.toList());

        /*List<Product> productsAll = productRepository.findProductsByStoreName(store.getName());
        return productsAll.stream()
                .filter(product -> product.getDescription().toLowerCase().matches("flatscreen\\s+tv\\s+\\d+\\s+inch"))
                .collect(Collectors.toList());*/
    }

    @Override
    public List<Product> getPremiumSmartphonesForStore(Store store) {
        List<Product> productsAll = productRepository.findProductsByStoreName(store.getName());
        return productsAll.stream()
                .filter(product -> product.getPrice().compareTo(BigDecimal.valueOf(1000)) > 1 )
                .collect(Collectors.toList());
    }



}
