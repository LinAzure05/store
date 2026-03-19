package com.crafts.platform.config;

import com.crafts.platform.interceptor.LoginInterceptor;
import com.crafts.platform.interceptor.RoleInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final RoleInterceptor roleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/home", "/merchant/**", "/admin/**")
                .excludePathPatterns("/auth/**", "/error", "/forbidden", "/css/**", "/js/**", "/images/**");

        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/merchant/**", "/admin/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/forbidden").setViewName("forbidden");
    }
}
