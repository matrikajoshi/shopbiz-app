package self.edu.shopbiz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.UserRepository;
import self.edu.shopbiz.service.UserService;

import java.util.Optional;

/**
 * Created by mpjoshi on 10/12/19.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getByid(Integer id){
        return userRepository.findById(id);
    }
}
