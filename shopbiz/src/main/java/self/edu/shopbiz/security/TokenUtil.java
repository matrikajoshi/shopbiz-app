package self.edu.shopbiz.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import self.edu.shopbiz.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static self.edu.shopbiz.security.SecurityConstants.*;

@Service
public class TokenUtil {

    public Optional<Authentication> verifyToken(HttpServletRequest request) {

      final String token = request.getHeader(AUTH_HEADER_NAME);

      if (token != null && !token.isEmpty()){
        final MyUserPrincipal myUserPrincipal = parseUserFromToken(token.replace("Bearer","").trim());
        if (myUserPrincipal != null) {
            return  Optional.of(new UsernamePasswordAuthenticationToken(myUserPrincipal, null, myUserPrincipal.getAuthorities()));
        }
      }
      return Optional.empty();

    }

    //Get User Info from the Token
    public MyUserPrincipal parseUserFromToken(String token){

        Claims claims = Jwts.parser()
            .setSigningKey(SECRET)
            .parseClaimsJws(token)
            .getBody();

        User user = new User();
        user.setEmail(claims.getSubject());
        user.setId((Integer) claims.get("userId"));
        MyUserPrincipal myUserPrincipal = new MyUserPrincipal(user);
        Set<SimpleGrantedAuthority> roles = getRolesFromString((String) claims.get("roles"));
        myUserPrincipal.setAuthorities(roles);

        if (user.getEmail() != null) {
            return myUserPrincipal;
        } else {
            return null;
        }
    }

    private Set<SimpleGrantedAuthority> getRolesFromString(String permissionStr) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        System.out.println(permissionStr);
        if(StringUtils.isNotEmpty(permissionStr)){
            String[] rolesArr = permissionStr.split(",");
            authorities = Arrays.stream(rolesArr)
                    .map((permission) -> new SimpleGrantedAuthority(permission))
                    .collect(Collectors.toSet());
        }
        return authorities;
    }


    public String createTokenForUser(User user, List<SimpleGrantedAuthority> authorities) {
      return Jwts.builder()
        .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
        .setSubject(user.getEmail())
        .claim("userId", user.getId())
        .claim("roles", getRolesStr(authorities))
        .signWith(SignatureAlgorithm.HS256, SECRET)
        .compact();
    }

    private String getRolesStr(List<SimpleGrantedAuthority> authorities){
        return authorities.stream().map(((role) -> role.getAuthority())).collect(Collectors.joining(","));
    }

}
