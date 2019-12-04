package self.edu.shopbiz.security;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by mpjoshi on 10/20/19.
 */

@Component
public class MyAuthenticationFailureHandler
        extends
        org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
                                        throws IOException, ServletException {


        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        JSONObject jsonResp = new JSONObject();
        try {
            jsonResp.put(
                    "timestamp",
                    Calendar.getInstance().getTime());
            jsonResp.put(
                    "exception",
                    exception.getMessage());
            jsonResp.put("status",HttpStatus.UNAUTHORIZED.value());
            jsonResp.put("message", getReason(exception));
            jsonResp.put("path", request.getRequestURL().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        response.getWriter().write(jsonResp.toString());
    }

    public String getReason(AuthenticationException exception) {
        String reason = "";
        if (exception instanceof BadCredentialsException
                || exception instanceof AuthenticationException) {
            reason = "user name or password is incorrect.";
        }
//        else if (exception instanceof DisabledException) {
//            reason = "Your account is disabled.";
//        } else if (exception instanceof AccountExpiredException) {
//            reason = "Your account is expired.";
//        } else {
//            reason = "Sorry, user name or password is incorrect.";
//        }
        return reason;
    }


}
