package local.authorization.resource.server.service;

import local.authorization.resource.server.model.Product;
import local.authorization.resource.server.model.Store;

import java.util.List;

public interface ProductService {

    List<Product> getFlatTvInStock();

    List<Product> getPremiumSmartphonesForStore(Store store);


}
