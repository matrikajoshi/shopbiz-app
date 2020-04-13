package edu.self.zuulproxy;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

public class CustomFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(CustomFilter.class);

    // pre, routing, post and error filters type
    @Override
    public String filterType() {
        // before request is sent to microservice
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //conditional
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        logger.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL()));
        System.out.println(Instant.now());
        context.set("starttime", Instant.now());
        return null;
    }
}
