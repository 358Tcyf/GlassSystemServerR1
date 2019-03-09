package project.ys.glass_system.model.s.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.s.entity.Glass;
import project.ys.glass_system.model.s.entity.Products;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductDao extends JpaRepository<Products, Integer> {

    Products findTopByModel(Glass model);

    List<Products> findProductsByDateBetween(LocalDateTime date, LocalDateTime date2);
}
