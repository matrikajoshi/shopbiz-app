package self.edu.shopbiz.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import self.edu.shopbiz.exceptionUtil.EmailNotUniqueException;
import self.edu.shopbiz.exceptionUtil.ResourceNotFoundException;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> signUp(@Valid @RequestBody User user) throws Exception {
        Optional<User> userOptional = this.userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new EmailNotUniqueException("User with this Email already exists");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    @RolesAllowed("MANAGE_PRODUCT")
    public @ResponseBody List<User> getAllUsers() {
        List<User> customers = userRepository.findAll();
        return customers;
    }

    @GetMapping("/{id}")
    @RolesAllowed("MANAGE_PRODUCT")
    public Resource<User> getCustomerById(@PathVariable("id") Integer id) {

        Optional<User> customerOptional = userRepository.findById(id);

        User user = customerOptional.orElseThrow(ResourceNotFoundException::new);

        //HATEOAS
        Resource<User> resource = new Resource<User>(user);

        ControllerLinkBuilder linkTo =
                linkTo(methodOn(this.getClass()).getAllUsers());

        resource.add(linkTo.withRel("all-users"));

        return resource;
    }


}
