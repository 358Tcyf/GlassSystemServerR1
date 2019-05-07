package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.t.entity.Factory;
import project.ys.glass_system.model.t.entity.Warehouse;

import java.util.List;

public interface WarehouseDao extends JpaRepository<Warehouse, Integer> {

    List<Warehouse> findByOwn(Factory own);
}
