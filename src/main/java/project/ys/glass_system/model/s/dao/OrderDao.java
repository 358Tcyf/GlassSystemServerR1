package project.ys.glass_system.model.s.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.s.entity.Orders;

import java.time.LocalDateTime;

public interface OrderDao extends JpaRepository<Orders, Integer> {

    Orders findByDate(LocalDateTime date);
}
