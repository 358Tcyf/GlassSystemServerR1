package project.ys.glass_system.model.t.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "gls_rank_table")
public class TestRank extends BaseEntity {

    public TestRank(){}
    public TestRank( String name){
        this.name=name;
    }

    @Column(name = "rank_name", nullable = false, unique = true)
    private String name;

    @Column(name = "rank_desc")
    private String desc;
}
