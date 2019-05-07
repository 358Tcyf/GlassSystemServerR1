package project.ys.glass_system.model.t.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.t.entity.*;

import java.util.Collection;
import java.util.List;

public interface GlassItemDao extends JpaRepository<GlassItem, Integer> {

    List<GlassItem> findByStoreAndProduceIn(WarehouseItem store, Collection<ProduceItem> produce);

    List<GlassItem> findByStoreIn(Collection<WarehouseItem> store);

    int countByRankAndTest(TestRank rank, TestItem test);

    int countByModelAndRankAndTest(GlassModel model, TestRank rank, TestItem test);

    List<GlassItem> findByTest(TestItem test);

    int countByStoreIn(Collection<WarehouseItem> store);

    int countByStore(WarehouseItem store);

    Page<GlassItem> queryByStore(WarehouseItem store, Pageable pageable);
}
