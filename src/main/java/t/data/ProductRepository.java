package t.data;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product,String> {
    Iterable<Product> findAll();

    Optional<Product> findById(String id);

    Product save(Product ingredient);
}
