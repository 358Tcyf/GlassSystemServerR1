package project.ys.glass_system.model.s.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class OrderItems extends BaseEntity {


    public OrderItems() {
    }

    public OrderItems(Glass model) {
        this(model, 0);
    }

    public OrderItems(Glass model, int appointment) {
        this(model, appointment, 0, 0);
    }

    public OrderItems(Glass model, int appointment, int delivery, double price) {
        this.model = model;
        this.appointment = appointment;
        this.delivery = delivery;
        this.price = price;
    }

    @OneToOne
    private Glass model;

    @Column(name = "aptm_num", length = 11)
    private int appointment;

    @Column(name = "dlv_num", length = 11)
    private int delivery;

    @Column(name = "item_price", length = 11)
    private double price;

    public Glass getModel() {
        return model;
    }

    public void setModel(Glass model) {
        this.model = model;
    }

    public int getAppointment() {
        return appointment;
    }

    public void setAppointment(int appointment) {
        this.appointment = appointment;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "model=" + model +
                ",\n appointment=" + appointment +
                ",\n delivery=" + delivery +
                ",\n price=" + price +
                '}';
    }
}
