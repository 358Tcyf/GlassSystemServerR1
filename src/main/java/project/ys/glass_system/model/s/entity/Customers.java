package project.ys.glass_system.model.s.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Customers extends BaseEntity {


    public static String[] CUST_PHONE = {"13200220164", "15206785319", "13602956738", "13100302561", "13102251966"};


    public Customers() {
    }

    public Customers(String name) {
        this(name, null, null);
    }

    public Customers(String name, String phone, String email) {
        this(name, phone, email, null);
    }

    private Customers(String name, String phone, String email, String city) {
        this(name, phone, email, null, city, null);
    }

    private Customers(String name, String phone, String email, String address, String city, String country) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.country = country;
    }


    @Column(name = "cust_name", nullable = false, length = 50)
    private String name;

    @Column(name = "cust_phone", length = 50)
    private String phone;

    @Column(name = "cust_email")
    private String email;

    @Column(name = "cust_add", length = 50)
    private String address;

    @Column(name = "cust_city", length = 50)
    private String city;

    @Column(name = "cust_country", length = 50)
    private String country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "name='" + name + '\'' +
                ",\n phone='" + phone + '\'' +
                ",\n email='" + email + '\'' +
                ",\n address='" + address + '\'' +
                ",\n city='" + city + '\'' +
                ",\n country='" + country + '\'' +
                '}';
    }
}
