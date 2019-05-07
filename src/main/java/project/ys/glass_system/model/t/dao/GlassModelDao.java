package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.ys.glass_system.model.t.entity.GlassModel;

import java.util.List;

public interface GlassModelDao extends JpaRepository<GlassModel, Integer> {

    @Query("select GlassModel.name from gls_model_table GlassModel")
    String[] findNameArray();

    @Query("select GlassModel.name from gls_model_table GlassModel")
    List<String> findNameList();
}
