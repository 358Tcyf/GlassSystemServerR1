package project.ys.glass_system.model.t.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity(name = "gls_produce_schedule")
public class Schedule extends BaseEntity {

    public Schedule() {
    }

    public Schedule(GlassModel model,ProduceNote belong, int num) {
        this.model = model;
        this.belong = belong;
        this.num = num;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private GlassModel model;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProduceNote belong;

    @Column(name = "schedule_num", length = 11)
    private int num;

    public GlassModel getModel() {
        return model;
    }

    public void setModel(GlassModel model) {
        this.model = model;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "model=" + model +
                ", belong=" + belong +
                ", num=" + num +
                '}';
    }
}
