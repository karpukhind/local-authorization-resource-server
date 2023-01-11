package local.authorization.resource.server.repositories;

import local.authorization.resource.server.model.Brand;
import local.authorization.resource.server.model.Store;
import local.authorization.resource.server.model.ProductionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByProductsDescriptionIgnoreCaseContaining(String descriptionToken);

    List<Store> findByBrandsProductionType(ProductionType productionType);


    /**
     * Doesn't work ; exception :
     * You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near
     * 'where product1_.description like '%sneakers%'' at line 1
     * @param descriptionToken
     * @return
     */
    @Query(value = "SELECT s from Store s join Product p where p.description like %:descriptionToken%")
    List<Store> findProductsDescriptionLikeQuery(@Param("descriptionToken") String descriptionToken);

}
