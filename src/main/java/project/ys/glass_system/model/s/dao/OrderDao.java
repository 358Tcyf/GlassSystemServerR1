package project.ys.glass_system.model.s.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.s.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDao extends JpaRepository<Orders, Integer> {

    Orders findByDate(LocalDateTime date);

    List<Orders> findOrdersByDateBetween(LocalDateTime date, LocalDateTime date2);


}
