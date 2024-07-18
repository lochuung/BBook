package com.huuloc.bookstore.bbook.controller.admin;

import com.huuloc.bookstore.bbook.entity.Order;
import com.huuloc.bookstore.bbook.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin/order/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("order", orderService.findById(id));
        return "admin/order/edit";
    }

    @PostMapping("/update")
    public ModelAndView update(@RequestParam("id") Long id, @RequestParam("state") String state) {
        Order order = orderService.updateState(id, state);
        return new ModelAndView("redirect:/admin/order/edit/" + order.getId());
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        orderService.delete(id);
        return new ModelAndView("redirect:/admin/order");
    }
}
