package self.edu.shopbiz.security;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by mpjoshi on 10/15/19.
 */

public class JWTAuthorizationFilter extends GenericFilterBean {

    private final TokenUtil tokenUtil;

    public JWTAuthorizationFilter(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;
        try {
            Optional<Authentication> authentication = tokenUtil.verifyToken(request);
            if (authentication.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(authentication.get());
            }
            else{
                SecurityContextHolder.getContext().setAuthentication(null);
            }
            filterChain.doFilter(request, response);
        }
        catch (JwtException e) {
            //when token is not valid
            logger.error("Authentication Error: {}", e.getMessage());
            logger.error(e.getStackTrace().toString());
            throw(e);
        }
        finally {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

}
