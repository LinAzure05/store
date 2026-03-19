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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/auth/login")
    public String loginPage(@RequestParam(value = "registered", required = false) Integer registered, Model model) {
        model.addAttribute("loginForm", new LoginRequest());
        if (registered != null && registered == 1) {
            model.addAttribute("success", "注册成功，请登录");
        }
        return "login";
    }

    @PostMapping("/auth/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginRequest loginRequest,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "请输入用户名和密码");
            return "login";
        }

        try {
            User user = userService.login(loginRequest);
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole());
            session.setAttribute("username", user.getUsername());
            return "redirect:/dashboard";
        } catch (BizException ex) {
            model.addAttribute("error", ex.getMessage());
            return "login";
        } catch (Exception ex) {
            model.addAttribute("error", "登录失败，请稍后重试");
            return "login";
        }
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
            model.addAttribute("error", "请检查输入内容后重新提交");
            return "register";
        }

        try {
            userService.register(registerRequest);
            return "redirect:/auth/login?registered=1";
        } catch (BizException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        } catch (Exception ex) {
            model.addAttribute("error", "注册失败，请稍后重试");
            return "register";
        }
    }

    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
