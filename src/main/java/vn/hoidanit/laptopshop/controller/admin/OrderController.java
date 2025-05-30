package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.Order_;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.OrderService;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/order")
    public String getDashboard(Model model,
            @RequestParam("page") Optional<String> pageOptional) {

        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Order_.ID).descending());
        Page<Order> prs = this.orderService.fetchOrders(pageable);
        List<Order> allOrders = prs.getContent();

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prs.getTotalPages());
        model.addAttribute("allOrders", allOrders);
        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        Order order = this.orderService.getOrderById(id);
        List<OrderDetail> orderDetails = order.getOrderDetails();
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("id", id);
        return "admin/order/detail";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable long id) {
        Order currentOrder = this.orderService.getOrderById(id);
        model.addAttribute("order", currentOrder);
        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")
    public String postUpdateOrder(Model model, @ModelAttribute("order") Order order) {
        this.orderService.updateOrder(order);
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteOrder(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("order", new Order());
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    public String postDeleteOrder(Model model, @ModelAttribute("order") Order deleteOrder) {
        this.orderService.deleteAOrder(deleteOrder.getId());

        return "redirect:/admin/order";
    }
}
