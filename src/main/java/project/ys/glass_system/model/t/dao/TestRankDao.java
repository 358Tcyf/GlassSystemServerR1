package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.ys.glass_system.model.t.entity.TestRank;

import java.util.List;

public interface TestRankDao extends JpaRepository<TestRank, Integer> {

    @Query("select TestRank from gls_rank_table TestRank ORDER BY TestRank.name")
    List<TestRank> findAllOrderByName();

    @Query("select TestRank.name from gls_rank_table TestRank ORDER BY TestRank.name")
    List<String> findNameListOrderByName();

}
