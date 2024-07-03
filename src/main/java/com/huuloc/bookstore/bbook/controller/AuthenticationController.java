package com.huuloc.bookstore.bbook.controller;

import com.huuloc.bookstore.bbook.dto.RegisterDto;
import com.huuloc.bookstore.bbook.service.auth.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @GetMapping({"/login"})
    public String getLoginPage(@RequestParam(name = "error", required = false) String error, Authentication authentication, Model model) {
        if (authentication != null) {
            return "redirect:/";
        }
        if (error != null) {
            model.addAttribute("error", "Đăng nhập không thành công!");
        }
        return "login";
    }

    @GetMapping({"/register"})
    public String getRegisterPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping({"/register"})
    public ModelAndView register(@ModelAttribute("registerDto") @Valid RegisterDto registerDto,
                                 BindingResult bindingResult) {
        if (registerDto.getConfirmPassword() != null
                && !registerDto.getConfirmPassword().equals(registerDto.getPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.registerDto",
                    "Mật khẩu không khớp");
        }
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("register");
            modelAndView.addObject("registerDto", registerDto);
            return modelAndView;
        }
        try {
            userService.register(registerDto);
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("register");
            modelAndView.addObject("registerDto", registerDto);
            modelAndView.addObject("error", e.getMessage());
            return modelAndView;
        }
        return new ModelAndView("redirect:/login");
    }
}
