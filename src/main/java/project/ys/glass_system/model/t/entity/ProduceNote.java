package project.ys.glass_system.model.t.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "gls_produce_note")
public class ProduceNote extends BaseEntity {

    @Column(name = "produce_note_date", unique = true, nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "belong_id")
    private List<Schedule> plans;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Schedule> getPlans() {
        return plans;
    }

    public void setPlans(List<Schedule> plans) {
        this.plans = plans;
    }
}
