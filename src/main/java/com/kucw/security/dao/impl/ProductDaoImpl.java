package com.kucw.security.dao.impl;

import com.kucw.security.dao.ProductDao;
import com.kucw.security.dto.ProductQueryParams;
import com.kucw.security.dto.ProductRequest;
import com.kucw.security.model.product.Product;
import com.kucw.security.rowmapper.ProductRowMapper;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Date;
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

    @Override
    public Integer createProduct(ProductRequest productRequest) {

        String sql = """
                INSERT INTO 
                product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
                VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int productId = keyHolder.getKey().intValue();

        return productId;

    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {

        String sql = """
                UPDATE product 
                SET product_name = :productName, category = :category, image_url = :imageUrl,
                    price = :price, stock = :stock, description = :description, last_modified_date = :lastModifiedDate
                WHERE product_id = :productId 
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);

    }

    @Override
    public void deleteProduct(Integer productId) {

        String sql = """
                DELETE FROM product WHERE product_id = :productId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);

    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {

        String sql = """
               SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date
               FROM product
               WHERE 1 = 1 
                """;

        Map<String, Object> map = new HashMap<>();

        // 查詢條件
        sql = getCondition(productQueryParams, sql, map);

        // 排序
        sql += " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        // 分頁
        sql += " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        return productList;
    }


    // 條件取得
    private String getCondition(ProductQueryParams productQueryParams, String sql, Map<String, Object> map) {
        // 商品類型條件
        if (productQueryParams.getCategory() != null) {
            sql += " AND category = :category";
            map.put("category", productQueryParams.getCategory().name());
        }
        // 搜尋框條件
        if (!StringUtils.isNullOrEmpty(productQueryParams.getSearch())) {
            sql += " AND product_name LIKE :product";
            map.put("product", "%" + productQueryParams.getSearch() + "%");
        }
        return sql;
    }

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {

        String sql = "SELECT count(*) FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        // 條件查詢
        sql = getCondition(productQueryParams, sql, map);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }
}
