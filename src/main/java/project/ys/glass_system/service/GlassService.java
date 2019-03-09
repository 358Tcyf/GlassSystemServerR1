package project.ys.glass_system.service;

import project.ys.glass_system.model.s.entity.Customers;
import project.ys.glass_system.model.s.entity.Glass;
import project.ys.glass_system.model.s.entity.OrderItems;
import project.ys.glass_system.model.s.entity.Products;

public interface GlassService {

    Glass getGlass(String model);

    Customers getCustomer(String phone);

    void addVirtualCustomer(Customers customers);

    void addVirtualGlass(Glass glass);

    Products virtualProduce(String model);

    OrderItems virtualSale(String model);

}
