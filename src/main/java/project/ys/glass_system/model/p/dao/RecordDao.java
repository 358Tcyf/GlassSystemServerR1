package project.ys.glass_system.model.p.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.Record;

public interface RecordDao extends JpaRepository<Record, Integer> {

    Page<Record> queryRecordsByActionBetween(int action, int action2, Pageable pageable);
}
