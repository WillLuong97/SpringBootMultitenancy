package com.willieluong.multitenant.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* To start, we’ll need a way to determine which tenant is making requests. We will use Spring Interceptor to intercept the HTTP request and get the tenant information from the HTTP header.
* The selected tenant is then stored in a ThreadLocal variable that is cleared after the request is completed.
* The interceptor gets the value of the “X-TenantID” HTTP header for every
* request and sets the current tenant inTenantContext class.
* If no header is provided, it responds with an error.  */
@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception{
        System.out.println("In preHandle, we are intercepting the request");
        System.out.println("_______________________________________________");
        String requestUri = request.getRequestURI();
        String tenantId = request.getHeader("X-TenantID");
        System.out.println("RequestUri::" + requestUri + "|| Search for X-TenantID:: " + tenantId);
        System.out.println("____________________________________________");

        //Tenant Id cannot be found.
        if(tenantId == null){
            response.getWriter().write("X-TenantID not present in the Request Header");
            response.setStatus(400);
            return false;
        }
        //importing custom tenant context to store the current tenant
        TenantContext.setCurrentTenant(tenantId);
        return true;
    }

    //after finished processing the tenant request, we will clear the current tenant out of the thread to make space for the next request.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        TenantContext.clear();
    }


}
