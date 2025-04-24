package vn.hoidanit.laptopshop.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAll(Pageable page);

    Order findById(long id);

    Order save(Order order);

    Order deleteById(long id);

    List<Order> findByUser(User user);

    Optional<Order> findByPaymentRef(String paymentRef);
}
