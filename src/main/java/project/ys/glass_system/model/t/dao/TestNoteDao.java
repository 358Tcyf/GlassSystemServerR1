package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.t.entity.TestNote;

import java.time.LocalDate;

public interface TestNoteDao extends JpaRepository<TestNote, Integer> {
    TestNote findByDate(LocalDate date);
}
