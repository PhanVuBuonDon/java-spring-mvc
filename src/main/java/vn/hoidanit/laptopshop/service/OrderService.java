package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;

@Controller
public class OrderService {

    public final OrderRepository orderRepository;
    public final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Page<Order> fetchOrders(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
    }

    public Order getOrderById(long id) {
        return this.orderRepository.findById(id);
    }

    public Order updateOrder(Order order) {
        Order currentOrder = this.orderRepository.findById(order.getId());
        currentOrder.setStatus(order.getStatus());
        return this.orderRepository.save(currentOrder);
    }

    public void deleteAOrder(long id) {
        List<OrderDetail> orderDetails = this.orderRepository.findById(id).getOrderDetails();

        for (OrderDetail od : orderDetails) {
            this.orderDetailRepository.deleteById(od.getId());
        }

        this.orderRepository.deleteById(id);
    }

    public List<Order> getOrderByUser(User user) {
        return this.orderRepository.findByUser(user);
    }
}
