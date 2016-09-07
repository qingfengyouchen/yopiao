package com.zx.stlife.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by micheal on 15/6/30.
 */
public class MyHiddenHttpMethodFilter extends HiddenHttpMethodFilter {

    private String methodParam = DEFAULT_METHOD_PARAM;

    public void setMethodParam(String methodParam){
        Assert.hasText(methodParam, "'methodParam' must not be empty");
        this.methodParam = methodParam;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String paramValue = request.getParameter(methodParam);
        String _method = request.getMethod();
        if (StringUtils.isNotBlank(paramValue)) {
            String method = paramValue.toUpperCase(Locale.ENGLISH);
            boolean b = ("POST".equals(_method) && "PUT".equalsIgnoreCase(method))
                    || ( "POST".equals(_method) && "DELETE".equalsIgnoreCase(method));
            if( b ){
                HttpServletRequest wrapper = new HttpMethodRequestWrapper(request, method);
                filterChain.doFilter(wrapper, response);
            }
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

        private final String method;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method;
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }
}
