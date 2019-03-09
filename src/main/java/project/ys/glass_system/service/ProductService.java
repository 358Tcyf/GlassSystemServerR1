package project.ys.glass_system.service;

import project.ys.glass_system.model.s.entity.Products;

import java.util.List;

public interface ProductService {

    void onceProduct(Products products);

    void dailyProduct(List<Products> products);

}
