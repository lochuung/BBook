package com.huuloc.bookstore.bbook.controller.customer;

import com.huuloc.bookstore.bbook.entity.User;
import com.huuloc.bookstore.bbook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModelAndView uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        userService.uploadProfilePicture(file);
        return new ModelAndView("redirect:/profile");
    }

    @GetMapping
    public String profile(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("address", userService.getDefaultAddress());
        return "customer/profile";
    }

    @PostMapping
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
