package com.kucw.security.service;

import com.kucw.security.dto.ProductRequest;
import com.kucw.security.model.product.Product;

public interface ProductService {

    /**
     * 取得商品 by productId
     * @param productId
     * @return Product
     **/
    Product getProductById(Integer productId);

    /**
     * 新增商品
     * @param productRequest
     * @return Integer
     **/
    Integer createProduct(ProductRequest productRequest);

    /**
     * 更新商品
     * @param  productId
     * @param productRequest
     **/
    void updateProduct(Integer productId, ProductRequest productRequest);

    /**
     * 刪除商品
     * @param  productId
     **/
    void deleteProduct(Integer productId);
}
