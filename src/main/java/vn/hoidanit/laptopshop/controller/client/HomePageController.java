package vn.hoidanit.laptopshop.controller.client;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.RegisterDTO;
import vn.hoidanit.laptopshop.service.OrderService;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class HomePageController {

    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;

    public HomePageController(ProductService productService, UserService userService, PasswordEncoder passwordEncoder,
            OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        // List<Product> allProducts = this.productService.fetchProducts();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> prs = this.productService.fetchProducts(pageable);
        List<Product> allProducts = prs.getContent();

        model.addAttribute("allProducts", allProducts);

        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());

        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
            BindingResult bindingResult) {

        // validate
        if (bindingResult.hasErrors()) {
            return "client/auth/register";
        }

        User user = this.userService.RegisterDTOtoUser(registerDTO);

        String hasPassword = this.passwordEncoder.encode(user.getPassword());

        user.setPassword(hasPassword);
        user.setRole(this.userService.getRoleByName("USER"));
        // save
        this.userService.handleSaveUser(user);
        return "redirect:/login";// redirect vào url
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "client/auth/login";
    }

    @GetMapping("/access-deny")
    public String getDenyPage(Model model) {
        return "client/auth/deny";
    }

    @GetMapping("/order-history")
    public String getHistoryPage(Model model, HttpServletRequest request) {
        User currenUser = new User();
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currenUser.setId(id);

        List<Order> orders = this.orderService.getOrderByUser(currenUser);

        model.addAttribute("orders", orders);
        return "client/auth/order-history";
    }

}
