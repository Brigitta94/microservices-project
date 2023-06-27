package com.brigi.orderservice.repository;

import com.brigi.orderservice.model.entity.OrderLineItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemsRepository extends JpaRepository<OrderLineItems, Integer> {
    List<OrderLineItems> findByOrderId(Integer orderID);
}
