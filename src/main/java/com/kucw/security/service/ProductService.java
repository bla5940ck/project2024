package com.kucw.security.service;

import com.kucw.security.model.product.Product;

public interface ProductService {

    /**
     * 取得商品 by productId
     * @param productId
     * @return Product
     **/
    Product getProductById(Integer productId);
}
