package local.authorization.resource.server.repositories;

import local.authorization.resource.server.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Product product SET product.store.id = :storeId WHERE product.id = :productId")
    int updateProductWithStore(@Param("storeId") long storeId, @Param("productId") long productId);

    List<Product> findProductsByStoreName(String storeName);

}
