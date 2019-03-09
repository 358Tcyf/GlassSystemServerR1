package project.ys.glass_system.model.s.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.s.entity.OrderItems;

public interface OrderItemDao extends JpaRepository<OrderItems, Integer> {


}
