package com.kucw.security.service.impl;

import com.kucw.security.dao.ProductDao;
import com.kucw.security.model.product.Product;
import com.kucw.security.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }
}
