package project.ys.glass_system.model.s.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Glass extends BaseEntity {


    public static String[] GLASS_MODEL = {"有色透明有机玻璃", "透明有机玻璃", "珠光有机玻璃", "压花有机玻璃"};

    public Glass() {
    }

    public Glass(String model) {
        this(model, 0);
    }

    public Glass(String model, int price) {
        this.model = model;
        this.price = price;
    }


    @Column(name = "gls_model", nullable = false, length = 50)
    private String model;

    @Column(name = "gls_price", length = 11)
    private int price;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "GlassItem{" +
                "model='" + model + '\'' +
                ",\n price=" + price +
                '}';
    }
}
