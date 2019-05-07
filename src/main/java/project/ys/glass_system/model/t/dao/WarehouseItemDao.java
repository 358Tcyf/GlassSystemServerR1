package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.t.entity.WarehouseItem;

import java.util.List;

public interface WarehouseItemDao extends JpaRepository<WarehouseItem, Integer> {

    List<WarehouseItem> findByTest(boolean test);

}
