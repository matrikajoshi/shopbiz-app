package self.edu.shopbiz.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import self.edu.shopbiz.exceptionUtil.ResourceNotFoundException;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

/**
 * Created by mpjoshi on 10/15/19.
 */

@RestController
@RequestMapping("/users")
@Api(tags = {"Users"})
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

    @GetMapping
    @RolesAllowed("MANAGE_PRODUCT")
    public @ResponseBody List<User> getAllUsers() {
        List<User> customers = userRepository.findAll();
        return customers;
    }

    @GetMapping("/{id}")
    @RolesAllowed("MANAGE_PRODUCT")
    public @ResponseBody User getCustomerById(@PathVariable("id") Integer id){
        Optional<User> customerOptional = userRepository.findById(id);
        return customerOptional.orElseThrow(ResourceNotFoundException::new);
    }


}
