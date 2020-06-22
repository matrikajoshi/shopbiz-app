package self.edu.shopbiz.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import self.edu.shopbiz.model.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static self.edu.shopbiz.security.SecurityConstants.*;


/**
 * Created by mpjoshi on 10/15/19.
 */


public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Value("${security.jwt.token.expire-length}")
    private long validityInSeconds;

    private final TokenUtil tokenUtil;
    private final String EMAIL_FIELD = "email";
    private final String PASSWORD_FIELD = "password";

    public JWTAuthenticationFilter(String urlMapping,
                                   AuthenticationManager authenticationManager,
                                   TokenUtil tokenUtil,
                                   AuthenticationFailureHandler authenticationFailureHandler) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authenticationManager);
        this.tokenUtil = tokenUtil;
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException, JSONException {
        try {
            // parse to json and create User object for loose coupling
            Map<String, String> creds = new ObjectMapper().readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {});
            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.get(EMAIL_FIELD),
                    creds.get(PASSWORD_FIELD));
            Authentication authentication = getAuthenticationManager().authenticate(authToken);
            return authentication;
        } catch (AuthenticationException e) {
            throw new AuthenticationServiceException(e.getMessage());
        } catch(IOException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(auth);

        if(auth instanceof UsernamePasswordAuthenticationToken){
            MyUserPrincipal myUserPrincipal = (MyUserPrincipal) auth.getPrincipal();
            User user = myUserPrincipal.getUser();

            List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) auth.getAuthorities();

            String tokenString = this.tokenUtil.createTokenForUser(user, authorities);

            res.addHeader(AUTH_HEADER_NAME, TOKEN_PREFIX + tokenString);

            JSONObject jsonResp = new JSONObject();
            jsonResp.put("email", user.getEmail());
            jsonResp.put("userName", user.getUserName());
            jsonResp.put("userId", user.getId());
            jsonResp.put("token", tokenString);
            jsonResp.put("roles", authorities.stream().map(perm -> perm.getAuthority()).collect(Collectors.toList()));
            jsonResp.put("expiresIn", this.tokenUtil.getValidityInSeconds());
            res.getWriter().write(jsonResp.toString());
            res.getWriter().flush();
            res.getWriter().close();
        }

    }

}
