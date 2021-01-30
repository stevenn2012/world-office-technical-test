package co.com.worldoffice.shoppingcart.domain.repository;

import co.com.worldoffice.shoppingcart.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends CrudRepository<User, Integer> {

    Optional<User> findFirstByIp(String ip);
}
