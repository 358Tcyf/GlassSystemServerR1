package project.ys.glass_system.model.t.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.t.entity.ProduceNote;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ProduceNoteDao extends JpaRepository<ProduceNote, Integer> {

    ProduceNote findByDate(LocalDate date);

    List<ProduceNote> findByDateIn(Collection<LocalDate> date);
}
