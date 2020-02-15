package self.edu.shopbiz.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import self.edu.shopbiz.dto.UserDTO;
import self.edu.shopbiz.exceptionUtil.EmailNotUniqueException;
import self.edu.shopbiz.exceptionUtil.ResourceNotFoundException;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Created by mpjoshi on 10/15/19.
 */

@RestController
@RequestMapping("/users")
@Tag(name="Users")
public class UserController {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    public UserController(UserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          ModelMapper modelMapper) {
        this.userRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> signUp(@Valid @RequestBody UserDTO userDTO) throws Exception {
        Optional<User> userOptional = this.userRepository.findByEmail(userDTO.getEmail());
        if (userOptional.isPresent()) {
            throw new EmailNotUniqueException("User with this Email already exists");
        }
        User user = convertToEntity(userDTO);
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
    public ResponseEntity<EntityModel<User>> getCustomerById(@PathVariable Integer id) {

        return userRepository.findById(id)
                .map(user -> new EntityModel<>(user,
                        linkTo(methodOn(UserController.class).getCustomerById(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAllUsers()).withRel("customers") ))
                .map(ResponseEntity::ok)
                .orElseThrow(ResourceNotFoundException::new);

    }

    private User convertToEntity(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return user;
    }
}
