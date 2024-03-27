package com.trademate.project.Repository;

import com.trademate.project.Model.OrdersModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<OrdersModel,Long> {
    OrdersModel findByOrderId(String order_id);
}
