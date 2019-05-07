package project.ys.glass_system.model.t.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "gls_test_table")
public class TestItem extends BaseEntity {

    public TestItem(){}
    public TestItem(LocalDateTime time,String uuid,TestNote belong){
        this.time=time;
        this.uuid=uuid;
        this.belong=belong;
    }

    @Column(name = "glass_test_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @Column(name = "test_uuid", nullable = false, unique = true)
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    private TestNote belong;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "belong_id")
    private List<TestResult> testResults;

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }
}
