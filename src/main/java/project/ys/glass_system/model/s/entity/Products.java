package project.ys.glass_system.model.s.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Products extends BaseEntity {


    public Products() {
    }

    public Products(Date produceDate) {
        this(produceDate, null, 0);
    }

    public Products(Date produceDate, String glassModel) {
        this(produceDate, glassModel, 0);
    }

    public Products(Date produceDate, String glassModel, int planNumber) {
        this(produceDate, glassModel, planNumber, 0, 0, 0);
    }

    public Products(Date produceDate, String glassModel, int planNumber, int hardenNumber, int coatNumber, int failNumber) {
        this.produceDate = produceDate;
        this.glassModel = glassModel;
        this.planNumber = planNumber;
        this.hardenNumber = hardenNumber;
        this.coatNumber = coatNumber;
        this.failNumber = failNumber;

    }

    @Column
    private Date produceDate;

    @Column
    private String glassModel;

    @Column
    private int planNumber;

    @Column
    private int hardenNumber;

    @Column
    private int coatNumber;

    @Column
    private int failNumber;

    public Date getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(Date produceDate) {
        this.produceDate = produceDate;
    }

    public String getGlassModel() {
        return glassModel;
    }

    public void setGlassModel(String glassModel) {
        this.glassModel = glassModel;
    }

    public int getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(int planNumber) {
        this.planNumber = planNumber;
    }

    public int getHardenNumber() {
        return hardenNumber;
    }

    public void setHardenNumber(int hardenNumber) {
        this.hardenNumber = hardenNumber;
    }

    public int getCoatNumber() {
        return coatNumber;
    }

    public void setCoatNumber(int coatNumber) {
        this.coatNumber = coatNumber;
    }

    public int getFailNumber() {
        return failNumber;
    }

    public void setFailNumber(int failNumber) {
        this.failNumber = failNumber;
    }
}
