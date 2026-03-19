package com.crafts.platform.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if ("admin".equals(role)) {
            return "redirect:/admin/dashboard";
        }
        if ("merchant".equals(role)) {
            return "redirect:/merchant/dashboard";
        }
        return "redirect:/home";
    }

    @GetMapping("/merchant/dashboard")
    public String merchantDashboard() {
        return "merchant-dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }
}
