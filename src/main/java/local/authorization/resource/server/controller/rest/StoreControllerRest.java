package local.authorization.resource.server.controller.rest;

import local.authorization.resource.server.model.Brand;
import local.authorization.resource.server.model.Store;
import local.authorization.resource.server.repositories.StoreRepository;
import local.authorization.resource.server.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rest/store")
public class StoreControllerRest {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreService storeService;

    @ResponseBody
    @PostMapping("/add")
    public String createNewStore(@RequestBody Store store) {
        Store storePersisted = storeRepository.save(store);
        return storePersisted.getId().toString();
    }

    @PostMapping("/{storeId}/brands")
    public void setBrandsToStore(@PathVariable String storeId, @RequestBody List<String> brandIds) {
        List<Long> brandIdsLong = brandIds.stream().map(Long::valueOf).collect(Collectors.toList());
        storeService.setOrAddBrands(Long.valueOf(storeId), brandIdsLong);
    }

    @GetMapping("/find")
    public List<Store> getStoresByProductDescriptions(@RequestParam String descriptionToken) {
        return storeRepository.findByProductsDescriptionIgnoreCaseContaining(descriptionToken);
    }


}
