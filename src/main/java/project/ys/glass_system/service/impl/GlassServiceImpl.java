package project.ys.glass_system.service.impl;

import org.springframework.stereotype.Service;
import project.ys.glass_system.model.s.dao.CustomerDao;
import project.ys.glass_system.model.s.dao.GlassDao;
import project.ys.glass_system.model.s.entity.Customers;
import project.ys.glass_system.model.s.entity.Glass;
import project.ys.glass_system.model.s.entity.OrderItems;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.GlassService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static project.ys.glass_system.util.RandomUtils.*;

@Service
@Transactional
public class GlassServiceImpl implements GlassService {

    @Resource
    CustomerDao customerDao;

    @Resource
    GlassDao glassDao;


    @Override
    public Glass getGlass(String model) {
        return glassDao.findGlassByModel(model);
    }

    @Override
    public Customers getCustomer(String phone) {
        return customerDao.findCustomersByPhone(phone);
    }

    @Override
    public void addVirtualCustomer(Customers customers) {
        if (customerDao.findCustomersByPhoneOrEmail(customers.getPhone(), customers.getEmail()) == null)
            customerDao.saveAndFlush(customers);
    }

    @Override
    public void addVirtualGlass(Glass glass) {
        if (glassDao.findGlassByModel(glass.getModel()) == null)
            glassDao.saveAndFlush(glass);
    }

    @Override
    public Products virtualProduce(String model) {
        int[] randomProduce = randomProduce();
        return new Products(LocalDateTime.now()
                , glassDao.findGlassByModel(model)
                , randomProduce[plan]
                , randomProduce[harden]
                , randomProduce[coat]
                , randomProduce[fail]);
    }

    @Override
    public OrderItems virtualSale(String model) {
        int[] randomSale = randomSale();
        return new OrderItems(glassDao.findGlassByModel(model)
                , randomSale[appointment]
                , randomSale[delivery]
                , glassDao.findGlassByModel(model).getPrice() * randomSale[delivery]);
    }


}
