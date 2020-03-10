package self.edu.shopbiz.service;

import self.edu.shopbiz.model.User;

import java.util.Optional;

/**
 * Created by mpjoshi on 10/12/19.
 */


public interface UserService {

    User createNewUser(User user);

    Optional<User> getByid(Integer id);
}
