package project.ys.glass_system.model.t.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity(name = "gls_test_result")
public class TestResult extends BaseEntity {
    public TestResult() {
    }

    public TestResult(TestItem belong,GlassModel model, TestRank rank, int num) {
        this.belong = belong;
        this.model = model;
        this.rank = rank;
        this.num = num;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private GlassModel model;

    @ManyToOne(fetch = FetchType.EAGER)
    private TestRank rank;

    @ManyToOne(fetch = FetchType.EAGER)
    private TestItem belong;

    @Column(name = "test_num", length = 11)
    private int num;
}
