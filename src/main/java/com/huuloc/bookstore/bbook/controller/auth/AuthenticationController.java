package com.huuloc.bookstore.bbook.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

    @GetMapping({"/login"})
    public String getLoginPage(@RequestParam(name = "error", required = false) String error,
                               Authentication authentication,
                               Model model) {
        if (authentication != null) {
            return "redirect:/";
        }
        if (error != null) {
            model.addAttribute("error", "Đăng nhập không thành công!");
        }
        return "login";
    }
}
