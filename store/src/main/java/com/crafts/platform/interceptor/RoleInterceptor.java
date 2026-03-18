package com.crafts.platform.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String role = (String) request.getSession().getAttribute("role");

        if (uri.startsWith("/admin") && !"admin".equals(role)) {
            response.sendRedirect("/forbidden");
            return false;
        }

        if (uri.startsWith("/merchant") && !"merchant".equals(role)) {
            response.sendRedirect("/forbidden");
            return false;
        }

        return true;
    }
}
