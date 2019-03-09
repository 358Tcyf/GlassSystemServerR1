package project.ys.glass_system.model.s.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Products extends BaseEntity {


    public Products() {
    }

    public Products(LocalDateTime date) {
        this(date, null, 0);
    }

    public Products(LocalDateTime date, Glass model) {
        this(date, model, 0);
    }

    public Products(LocalDateTime date, Glass model, int plan) {
        this(date, model, plan, 0, 0, 0);
    }

    public Products(LocalDateTime date, Glass model, int plan, int harden, int coat, int fail) {
        this.date = date;
        this.model = model;
        this.plan = plan;
        this.harden = harden;
        this.coat = coat;
        this.fail = fail;

    }

    @Column(name = "produce_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @OneToOne
    private Glass model;

    @Column(name = "plan_num", length = 11)
    private int plan;

    @Column(name = "harden_num", length = 11)
    private int harden;

    @Column(name = "coat_num", length = 11)
    private int coat;

    @Column(name = "fail_num", length = 11)
    private int fail;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Glass getModel() {
        return model;
    }

    public void setModel(Glass model) {
        this.model = model;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getHarden() {
        return harden;
    }

    public void setHarden(int harden) {
        this.harden = harden;
    }

    public int getCoat() {
        return coat;
    }

    public void setCoat(int coat) {
        this.coat = coat;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    @Override
    public String toString() {
        return "Products{" +
                "date=" + date +
                ",\n model=" + model +
                ",\n plan=" + plan +
                ",\n harden=" + harden +
                ",\n coat=" + coat +
                ",\n fail=" + fail +
                '}';
    }
}
