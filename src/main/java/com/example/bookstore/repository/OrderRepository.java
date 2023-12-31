package com.example.bookstore.repository;

import com.example.bookstore.model.Order;
import com.example.bookstore.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {
    List<Order> findAllByUser(User user, Pageable pageable);
}
