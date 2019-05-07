package project.ys.glass_system.service.impl;

import org.springframework.stereotype.Service;
import project.ys.glass_system.model.s.dao.ProductDao;
import project.ys.glass_system.model.s.dao.ProductNoteDao;
import project.ys.glass_system.model.s.entity.ProductNotes;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.ProductService;
import project.ys.glass_system.util.RandomUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ProductServiceImpl implements ProductService {


    @Resource
    private ProductDao productDao;

    @Resource
    private ProductNoteDao productNoteDao;


    @Override
    public void onceProduct(Products product) {
        productDao.save(product);
        ProductNotes productNotes;
        List<Products> list;
        LocalDate date = product.getDate().toLocalDate();
        if (null != productNoteDao.findByDate(date)) {
            productNotes = productNoteDao.findByDate(date);
            list = productNotes.getProducts();
            list.add(product);
            productNotes.setProducts(list);
        } else {
            productNotes = new ProductNotes(date);
            list = new ArrayList<>();
            list.add(product);
            productNotes.setProducts(list);
        }
        productNotes.setWater(productNotes.getWater() + RandomUtils.randomInt(0.10, 0.50));
        productNotes.setElectricity(productNotes.getElectricity() + RandomUtils.randomInt(1.00, 3.00));
        productNotes.setCoal(productNotes.getCoal() + RandomUtils.randomInt(0.50, 1.00));
        productNoteDao.saveAndFlush(productNotes);
    }

    @Override
    public void dailyProduct(List<Products> products) {
        for (Products product : products) {
            productDao.save(product);
            onceProduct(product);
        }

    }


}
