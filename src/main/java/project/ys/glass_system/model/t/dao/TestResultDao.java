package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.ys.glass_system.model.t.entity.GlassModel;
import project.ys.glass_system.model.t.entity.TestItem;
import project.ys.glass_system.model.t.entity.TestRank;
import project.ys.glass_system.model.t.entity.TestResult;

import java.util.Collection;

public interface TestResultDao extends JpaRepository<TestResult, Integer> {

    @Query("SELECT COALESCE(SUM(result.num),0) FROM gls_test_result result WHERE result.belong IN ?1 AND result.model =?2  AND result.rank =?3")
    int sumNumByBelongInAndModelAndRank(Collection<TestItem> belong, GlassModel model, TestRank rank);

    @Query("SELECT COALESCE(SUM(result.num),0) FROM gls_test_result result WHERE result.belong IN ?1 AND result.rank =?2")
    int sumNumByBelongInAndRank(Collection<TestItem> belong, TestRank rank);

}
