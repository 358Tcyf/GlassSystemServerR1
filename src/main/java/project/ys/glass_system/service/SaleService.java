package project.ys.glass_system.service;

import org.hibernate.criterion.Order;
import project.ys.glass_system.model.s.entity.Customers;
import project.ys.glass_system.model.s.entity.OrderItems;
import project.ys.glass_system.model.s.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {

    Orders oneOrder(LocalDateTime date, Customers customer, OrderItems item);

    Orders ordersOfCustomer(Orders orders, List<OrderItems> items);

    Orders ordersOfCustomer(Orders orders, OrderItems item);

}
