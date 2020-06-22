package self.edu.shopbiz.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import self.edu.shopbiz.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static self.edu.shopbiz.security.SecurityConstants.*;

@Component
public class TokenUtil {

    @Value("${security.jwt.token.secret-key}")
    private  String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInSeconds;


    public long getValidityInSeconds() {
        return validityInSeconds;
    }


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
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        User user = new User();
        user.setEmail(claims.getSubject());
        user.setId((Integer) claims.get("userId"));
        user.setUserName((String) claims.get("userName"));
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
        if (StringUtils.isNotEmpty(permissionStr)) {
            String[] rolesArr = permissionStr.split(",");
            authorities = Arrays.stream(rolesArr)
                    .map((permission) -> new SimpleGrantedAuthority(permission))
                    .collect(Collectors.toSet());
        }
        return authorities;
    }


    public String createTokenForUser(User user, List<SimpleGrantedAuthority> authorities) {
        Date validity = new Date(new Date().getTime() + validityInSeconds);
        return Jwts.builder()
                .setExpiration(validity)
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("userName", user.getUserName())
                .claim("roles", getRolesStr(authorities))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private String getRolesStr(List<SimpleGrantedAuthority> authorities){
        return authorities.stream().map(((role) -> role.getAuthority())).collect(Collectors.joining(","));
    }

}
