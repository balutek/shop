package pl.balutek.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.balutek.model.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {

}
