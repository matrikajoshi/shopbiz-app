package self.edu.shopbiz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import self.edu.shopbiz.model.Role;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.RoleRepository;
import self.edu.shopbiz.repository.UserRepository;
import self.edu.shopbiz.service.UserService;

import java.util.Collections;
import java.util.Optional;

/**
 * Created by mpjoshi on 10/12/19.
 */

@Service
public class UserServiceImpl implements UserService {

    private static final String ROLE_CUSTOMER = "CUSTOMER";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User createNewUser(User user) {
        Role role = roleRepository.getByName(ROLE_CUSTOMER);
        user.setRoles(Collections.singleton(role));
        User save = userRepository.save(user);
        return save;
    }

    @Override
    public Optional<User> getByid(Integer id){
        return userRepository.findById(id);
    }
}
