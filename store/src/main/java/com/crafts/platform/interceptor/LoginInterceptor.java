package com.crafts.platform.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userId = request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("/auth/login");
            return false;
        }
        return true;
    }
}
