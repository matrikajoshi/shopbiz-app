package self.edu.shopbiz.security;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by mpjoshi on 10/21/19.
 */

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public RestAuthenticationEntryPoint() {
    }

    @Override
    public final void commence(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final AuthenticationException authException) throws IOException {
        //TODO set any error as json and log , may need to send it to client.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        JSONObject jsonResp = new JSONObject();
        try {
            jsonResp.put(
                    "timestamp",
                    Calendar.getInstance().getTime());
            jsonResp.put(
                    "exception",
                    authException.getMessage());
            jsonResp.put("status", HttpStatus.UNAUTHORIZED.value());
            jsonResp.put("path", request.getRequestURL().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        response.getWriter().write(jsonResp.toString());
    }

}

