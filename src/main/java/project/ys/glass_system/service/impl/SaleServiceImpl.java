package project.ys.glass_system.service.impl;

import org.springframework.stereotype.Service;
import project.ys.glass_system.model.s.dao.OrderDao;
import project.ys.glass_system.model.s.dao.OrderItemDao;
import project.ys.glass_system.model.s.dao.SaleNoteDao;
import project.ys.glass_system.model.s.entity.Customers;
import project.ys.glass_system.model.s.entity.OrderItems;
import project.ys.glass_system.model.s.entity.Orders;
import project.ys.glass_system.model.s.entity.SaleNotes;
import project.ys.glass_system.service.SaleService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class SaleServiceImpl implements SaleService {

    @Resource
    OrderItemDao orderItemDao;

    @Resource
    OrderDao orderDao;

    @Resource
    SaleNoteDao saleNoteDao;


    private OrderItems orderItem(OrderItems item) {
        return orderItemDao.saveAndFlush(item);
    }

    @Override
    public Orders oneOrder(LocalDateTime date, Customers customer, OrderItems item) {
        Orders orders;
        List<OrderItems> list = new ArrayList<>();
        orders = new Orders(date, customer);
        list.add(orderItem(item));
        orders.setOrderItems(list);
        return orderDao.saveAndFlush(orders);
    }

    @Override
    public Orders ordersOfCustomer(Orders orders, OrderItems item) {
        orders = oneOrder(orders.getDate(), orders.getCustomer(), item);
        return orders;
    }


    @Override
    public Orders ordersOfCustomer(Orders orders, List<OrderItems> items) {
        List<OrderItems> list = new ArrayList<>();
        for (OrderItems item : items) {
            list.add(orderItem(item));
            orders.setPrice(orders.getPrice() + item.getPrice());
        }
        orders.setOrderItems(list);
        return orderDao.saveAndFlush(orders);
    }


    public void ordersOfDay(Orders orders) {
        SaleNotes saleNote;
        List<Orders> list;
        LocalDate date = orders.getDate().toLocalDate();
        if (null != saleNoteDao.findByDate(date)) {
            saleNote = saleNoteDao.findByDate(date);
            list = saleNote.getOrders();
            if (list.contains(orders))
                return;
            list.add(orders);
        } else {
            saleNote = new SaleNotes(date);
            list = new ArrayList<>();
            list.add(orders);
        }
        saleNote.setOrders(list);
        saleNoteDao.saveAndFlush(saleNote);
    }


}
