package project.ys.glass_system.model.s.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class Orders extends BaseEntity {

    public Orders() {
    }

    public Orders(LocalDateTime date) {
        this(date, null);
    }


    public Orders(LocalDateTime date, Customers customer) {
        this.date = date;
        this.customer = customer;
    }

    @Column(name = "order_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @OneToOne
    private Customers customer;

    @OneToMany
    private List<OrderItems> orderItems;

    @Column(name = "order_price", length = 11)
    private double price;

    @Column(name = "order_rate", length = 11)
    private double rate;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "date=" + date +
                ",\n customer=" + customer +
                ",\n orderItems=" + orderItems +
                ",\n price=" + price +
                ",\n rate=" + rate +
                '}';
    }
}
