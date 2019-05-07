package project.ys.glass_system.model.t.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "gls_test_note")
public class TestNote extends BaseEntity {

    @Column(name = "test_note_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "belong_id")
    private List<TestItem> testItems;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TestItem> getTestItems() {
        return testItems;
    }

    public void setTestItems(List<TestItem> testItems) {
        this.testItems = testItems;
    }
}
