package project.ys.glass_system.service.impl;

import org.springframework.stereotype.Service;
import project.ys.glass_system.model.s.dao.ProductDao;
import project.ys.glass_system.model.s.dao.ProductNoteDao;
import project.ys.glass_system.model.s.entity.ProductNotes;
import project.ys.glass_system.model.s.entity.Products;
import project.ys.glass_system.service.ProductService;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static project.ys.glass_system.utils.DateUtil.accurateToDate;


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
        Date date = accurateToDate(product.getProduceDate());
        if (null != productNoteDao.findByProduceDate(date)) {
            productNotes = productNoteDao.findByProduceDate(date);
            list = productNotes.getProducts();
            list.add(product);
            productNotes.setProducts(list);

        } else {
            productNotes = new ProductNotes(date);
            list = new ArrayList<>();
            list.add(product);
            productNotes.setProducts(list);
        }
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
