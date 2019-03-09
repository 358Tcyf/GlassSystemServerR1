package project.ys.glass_system.model.s.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.s.entity.ProductNotes;

import java.time.LocalDate;
import java.util.Date;

public interface ProductNoteDao extends JpaRepository<ProductNotes, Integer> {

    ProductNotes findByDate(LocalDate date);

}
