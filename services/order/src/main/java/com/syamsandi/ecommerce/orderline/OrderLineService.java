package com.syamsandi.ecommerce.orderline;

import com.syamsandi.ecommerce.order.Order;
import com.syamsandi.ecommerce.order.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;
    private final OrderRepository orderRepository;
    public Integer saveOrderLine(OrderLineRequest request) {
        Order order = orderRepository.findById(request.orderId()).orElseThrow(
                () -> new EntityNotFoundException("Order Not Found")
        );
        OrderLine orderLine = mapper.toOrderLine(request,order);
        return repository.save(orderLine).getId();
    }
}
