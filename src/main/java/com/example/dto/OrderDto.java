package com.example.dto;

import com.example.entity.OrderDetail;
import com.example.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails;
}
