package self.edu.shopbiz.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mpjoshi on 11/2/19.
 * Handler for security error on protected resources
 */



public class RestAuthenticationAccessDeniedHandler implements AccessDeniedHandler {

    public RestAuthenticationAccessDeniedHandler() {
        super();
    }

    @Override
    public void handle(HttpServletRequest arg0, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }
}
