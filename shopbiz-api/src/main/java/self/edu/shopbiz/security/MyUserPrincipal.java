package self.edu.shopbiz.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import self.edu.shopbiz.model.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mpjoshi on 11/1/19.
 */


public class MyUserPrincipal implements UserDetails{

    private User user;
    private boolean enabled;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private Set<SimpleGrantedAuthority> authorities;

    public MyUserPrincipal(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.authorities == null){
            authorities = user.getRoles()
                    .stream()
                    .flatMap(role -> role.getPermissions()
                            .stream()
                            .map(permission ->
                                    new SimpleGrantedAuthority(permission.getName())))
                    .collect(Collectors.toSet());
        }

        return authorities;
    }

    public void setAuthorities(Set<SimpleGrantedAuthority> authorities){
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }
}
