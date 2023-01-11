package local.authorization.resource.server.repositories;

import local.authorization.resource.server.model.Brand;
import org.springframework.data.repository.CrudRepository;

public interface BrandRepository extends CrudRepository<Brand, Long> {
}
