package com.syamsandi.ecommerce.orderline;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-lines")
public class OrderLineController {
    private OrderLineService service;


    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderLineResponse>> findAllByOrderId(
            @PathVariable("order-id") Integer orderId
    ){
        return ResponseEntity.ok(service.findAllByOrderId(orderId));
    }
}
