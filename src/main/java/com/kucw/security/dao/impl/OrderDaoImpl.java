package com.kucw.security.dao.impl;

import com.kucw.security.dao.OrderDao;
import com.kucw.security.dto.CreateOrderRequest;
import com.kucw.security.model.order.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createOrder(Integer memberId, Integer totalAmount) {
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
}
