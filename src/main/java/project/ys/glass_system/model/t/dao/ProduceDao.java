package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.ys.glass_system.model.t.entity.GlassModel;
import project.ys.glass_system.model.t.entity.ProduceItem;
import project.ys.glass_system.model.t.entity.ProduceNote;

import java.util.List;

public interface ProduceDao extends JpaRepository<ProduceItem, Integer> {


    @Query("SELECT COALESCE(SUM(produceItem.water),0) FROM gls_produce_table produceItem WHERE produceItem.belong = ?1")
    float sumWaterByBelong(ProduceNote belong);

    @Query("SELECT COALESCE(SUM(produceItem.electricity),0) FROM gls_produce_table produceItem WHERE produceItem.belong = ?1")
    float sumElectricityByBelong(ProduceNote belong);

    @Query("SELECT COALESCE(SUM(produceItem.material),0) FROM gls_produce_table produceItem WHERE produceItem.belong = ?1")
    float sumMaterialByBelong(ProduceNote belong);

    @Query("SELECT COALESCE(SUM(produceItem.coal),0) FROM gls_produce_table produceItem WHERE produceItem.belong = ?1")
    float sumCoalByBelong(ProduceNote belong);

    @Query("SELECT COALESCE(SUM(produceItem.num),0) FROM gls_produce_table produceItem WHERE produceItem.belong = ?1 AND produceItem.model =?2")
    int sumNumByBelongAndModel(ProduceNote belong, GlassModel model);

    List<ProduceItem> findByBelong(ProduceNote belong);

}
