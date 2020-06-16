package self.edu.shopbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import self.edu.shopbiz.model.User;

import java.util.Optional;

/**
 * Created by mpjoshi on 10/15/19.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}