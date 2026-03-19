package com.crafts.platform.controller;

import com.crafts.platform.dto.LoginRequest;
import com.crafts.platform.dto.RegisterRequest;
import com.crafts.platform.entity.User;
import com.crafts.platform.exception.BizException;
import com.crafts.platform.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/auth/login")
    public String loginPage(Model model) {
        model.addAttribute("loginForm", new LoginRequest());
        return "login";
    }

    @PostMapping("/auth/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginRequest loginRequest,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            User user = userService.login(loginRequest);
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole());
            session.setAttribute("username", user.getUsername());
        } catch (BizException ex) {
            model.addAttribute("error", ex.getMessage());
            return "login";
        }

        String role = (String) session.getAttribute("role");
        if ("admin".equals(role)) {
            return "redirect:/admin/dashboard";
        }
        if ("merchant".equals(role)) {
            return "redirect:/merchant/dashboard";
        }
        return "redirect:/home";
    }

    @GetMapping("/auth/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterRequest());
        return "register";
    }

    @PostMapping("/auth/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterRequest registerRequest,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.register(registerRequest);
            model.addAttribute("success", "注册成功，请登录");
            model.addAttribute("loginForm", new LoginRequest());
            return "login";
        } catch (BizException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
