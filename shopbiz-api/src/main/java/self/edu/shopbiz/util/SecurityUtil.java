package self.edu.shopbiz.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import self.edu.shopbiz.model.User;
import self.edu.shopbiz.security.MyUserPrincipal;

/**
 * Created by mpjoshi on 11/2/19.
 */


public class SecurityUtil {

    public static Integer getLoggedInUserId() {
        return getLoggedInUser().getUser().getId();
    }

    public static MyUserPrincipal getLoggedInUser(){
        MyUserPrincipal userDetails = (MyUserPrincipal) getAuthentication().getPrincipal();
        return userDetails;
    }

    public static Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;

    }

}
