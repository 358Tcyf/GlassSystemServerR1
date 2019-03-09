package project.ys.glass_system.model.s.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class ProductNotes extends BaseEntity {

    public ProductNotes() {
    }

    public ProductNotes(LocalDate date) {
        this(date, 0, 0);
    }


    public ProductNotes(LocalDate date, double water, double electricity) {
        this.date = date;
        this.water = water;
        this.electricity = electricity;
    }

    @Column(name = "note_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @OneToMany
    private List<Products> products;

    @Column(name = "wtr_consu", length = 11)
    private double water;

    @Column(name = "elec_consu", length = 11)
    private double electricity;

    public LocalDate getProduceDate() {
        return date;
    }

    public void setProduceDate(LocalDate noteDate) {
        this.date = noteDate;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
    }

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }
}
