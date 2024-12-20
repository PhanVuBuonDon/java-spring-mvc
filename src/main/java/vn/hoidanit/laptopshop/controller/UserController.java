package vn.hoidanit.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {
    // DI dependency Injection
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        // String test = this.userService.handleHello();
        // model.addAttribute("eric", test);
        // model.addAttribute("hoidanit", "from controller with model");
        // user sevice nối với jsp thông qua model
        // eric là phần thêm vào
        // test là user service
        return "hello";// web động
    }

    @RequestMapping("/admin/user")
    public String createUser(Model model) {

        return "admin/user/create";
    }
}

// // @RestController
// // public class UserController {
// // DI dependency Injection
// private UserService userService;

// public UserController(UserService userService) {
// this.userService = userService;
// }

// @GetMapping("")
// public String getHomePage() {
// return userService.handleHello();
// }
// }
