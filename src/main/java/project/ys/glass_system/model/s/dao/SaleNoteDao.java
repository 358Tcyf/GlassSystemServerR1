package project.ys.glass_system.model.s.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.s.entity.SaleNotes;

import java.time.LocalDate;

public interface SaleNoteDao extends JpaRepository<SaleNotes, Integer> {

    SaleNotes findByDate(LocalDate date);

}
