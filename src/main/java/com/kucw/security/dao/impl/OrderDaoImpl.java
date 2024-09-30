package com.kucw.security.dao.impl;

import com.kucw.security.dao.OrderDao;
import com.kucw.security.dto.OrderQueryParams;
import com.kucw.security.model.order.Order;
import com.kucw.security.model.order.OrderItem;
import com.kucw.security.rowmapper.OrderItemRowMapper;
import com.kucw.security.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createOrder(Integer memberId, BigDecimal totalAmount) {
        String sql = """
                INSERT INTO `order`(member_id, total_amount, created_date, last_modified_date)
                VALUES (:memberId, :totalAmount, :createdDate, :lastModifiedDate)
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("totalAmount", totalAmount);

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int order = keyHolder.getKey().intValue();

        return order;
    }

    @Override
    public void createItems(Integer orderId, List<OrderItem> orderItemList) {

        String sql = """
                INSERT INTO order_item (order_id, product_id , quantity, amount)
                VALUES (:orderId, :productId , :quantity, :amount)
                """;

        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("orderId", orderId);
            mapSqlParameterSource.addValue("productId", orderItem.getProductId());
            mapSqlParameterSource.addValue("quantity", orderItem.getQuantity());
            mapSqlParameterSource.addValue("amount", orderItem.getAmount());
            parameterSources[i] = mapSqlParameterSource;
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);

    }

    @Override
    public Order getOrderById(Integer orderId) {

        String sql = """
                SELECT order_Id, member_id, total_amount, created_date, last_modified_date  
                FROM `order` 
                WHERE order_id = :orderId               
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        if (!CollectionUtils.isEmpty(orderList)) {
            return orderList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemListByOrderId(Integer orderId) {

        String sql = """
                SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url
                FROM order_item oi 
                JOIN product p
                ON oi.product_id = p.product_id
                WHERE oi.order_id = :orderId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        return orderItemList;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {

        String sql = """
                SELECT order_Id, member_id, total_amount, created_date, last_modified_date 
                FROM `order`
                WHERE 1=1
                """;

        Map<String, Object> map = new HashMap<>();

        // 條件
        sql = getCondition(orderQueryParams, sql, map);

        // 排序
        sql = sql + " ORDER BY created_date DESC";

        // 分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", orderQueryParams.getLimit());
        map.put("offset", orderQueryParams.getOffset());

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        return orderList;
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = """
                SELECT count(*)
                FROM `order`
                WHERE 1=1
                """;

        Map<String, Object> map = new HashMap<>();

        sql = getCondition(orderQueryParams, sql, map);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return count;
    }

    private String getCondition(OrderQueryParams orderQueryParams, String sql, Map<String, Object> map) {
        if (orderQueryParams.getMemberId() != null) {
            sql += " AND member_id = :memberId";
            map.put("memberId", orderQueryParams.getMemberId());
        }
        return sql;
    }
}
