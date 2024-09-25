package com.kucw.security.dao.impl;

import com.kucw.security.dao.ProductDao;
import com.kucw.security.model.product.Product;
import com.kucw.security.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product getProductById(Integer productId) {

        String sql = """
               SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date
               FROM product
               WHERE product_id = :productId 
               """;

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if (!CollectionUtils.isEmpty(productList)) {
            return productList.get(0);
        } else {
            return null;
        }
    }
}
