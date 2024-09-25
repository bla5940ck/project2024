package com.kucw.security.dao;

import com.kucw.security.model.product.Product;

public interface ProductDao {

    /**
     * 取得商品 by productId
     * @param productId
     * @return Product
     **/
    Product getProductById(Integer productId);
}
