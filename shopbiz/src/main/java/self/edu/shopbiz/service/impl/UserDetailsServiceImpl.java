package self.edu.shopbiz.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import self.edu.shopbiz.model.Permission;
import self.edu.shopbiz.model.Role;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.repository.UserRepository;
import self.edu.shopbiz.security.MyUserPrincipal;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

/**
 * Created by mpjoshi on 10/15/19.
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        MyUserPrincipal myUserPrincipal = new MyUserPrincipal(user);
        myUserPrincipal.setEnabled(true);
        myUserPrincipal.setAccountNonExpired(true);
        myUserPrincipal.setAccountNonLocked(true);
        myUserPrincipal.setCredentialsNonExpired(true);

        return myUserPrincipal;
    }


    public Set<SimpleGrantedAuthority> getUserPermission(User user){

        Set<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions()
                        .stream()
                        .map(permission ->
                                new SimpleGrantedAuthority(permission.getName())))
                .collect(Collectors.toSet());

        return authorities;

        /**
         * Set<SimpleGrantedAuthority> authorities = new HashSet<>();
         for(Role role: user.getRoles()){
         Set<SimpleGrantedAuthority> grantedAuthorities = role.getPermissions().stream().map(
         permission ->
         new SimpleGrantedAuthority(permission.getName()))
         .collect(Collectors.toSet());
         authorities.addAll(grantedAuthorities);
         }
         */
    }



}
