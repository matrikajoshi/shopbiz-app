package self.edu.shopbiz.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by mpjoshi on 10/15/19.
 */

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody User user) {
        Optional<User> userOptional = this.userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            return "EXISTS";
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "OK";
    }

    //TO DO add security
    @GetMapping(path="/all")
    public @ResponseBody List<User> getAllUsers() {
        List<User> customers = userRepository.findAll();
        return customers;
    }

    @GetMapping("/{id}")
    public @ResponseBody Optional<User> getCustomerById(@PathVariable("id") Integer id){
        Optional<User> customerOptional = userRepository.findById(id);
        return customerOptional;
    }


}
