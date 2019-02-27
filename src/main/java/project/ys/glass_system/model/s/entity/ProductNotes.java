package project.ys.glass_system.model.s.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class ProductNotes extends BaseEntity {

    public ProductNotes() {
    }

    public ProductNotes(Date produceDate) {
        this.produceDate = produceDate;
    }


    @Column
    private Date produceDate;

    @OneToMany
    List<Products> products;

    @Column
    private double waterConsumption;

    @Column
    private double electricityConsumption;

    public Date getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(Date produceDate) {
        this.produceDate = produceDate;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public double getWaterConsumption() {
        return waterConsumption;
    }

    public void setWaterConsumption(double waterConsumption) {
        this.waterConsumption = waterConsumption;
    }

    public double getElectricityConsumption() {
        return electricityConsumption;
    }

    public void setElectricityConsumption(double electricityConsumption) {
        this.electricityConsumption = electricityConsumption;
    }
}
