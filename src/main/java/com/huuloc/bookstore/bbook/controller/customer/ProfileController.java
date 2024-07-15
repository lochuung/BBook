package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.entity.User;
import com.huuloc.bookstore.bbook.service.auth.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("address", userService.getDefaultAddress());
        return "customer/profile";
    }

    @PostMapping("/profile")
    public ModelAndView updateProfile(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.addObject("address", userService.getDefaultAddress());
            modelAndView.setViewName("customer/profile");
            return modelAndView;
        }
        userService.updateProfile(user);
        return new ModelAndView("redirect:/profile");
    }
}
