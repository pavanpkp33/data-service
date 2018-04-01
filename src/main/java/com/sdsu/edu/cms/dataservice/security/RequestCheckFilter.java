package com.sdsu.edu.cms.dataservice.security;


import com.sdsu.edu.cms.dataservice.util.Constants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestCheckFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if(httpRequest.getHeader(Constants.INTERNAL_TOKEN) != null &&
                httpRequest.getHeader(Constants.INTERNAL_TOKEN).equals(Constants.INTERNAL_TOKEN_VALUE)){
            chain.doFilter(httpRequest, response);
        }else{
            ((HttpServletResponse) response)
                    .setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unknown request originator");


        }



    }

    @Override
    public void destroy() {

    }
}
