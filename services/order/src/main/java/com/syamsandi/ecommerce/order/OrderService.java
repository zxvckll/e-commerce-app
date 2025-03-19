package com.syamsandi.ecommerce.order;

import com.syamsandi.ecommerce.customer.CustomerClient;
import com.syamsandi.ecommerce.customer.CustomerResponse;
import com.syamsandi.ecommerce.exception.BusinessException;
import com.syamsandi.ecommerce.orderline.OrderLineRequest;
import com.syamsandi.ecommerce.orderline.OrderLineService;
import com.syamsandi.ecommerce.product.ProductClient;
import com.syamsandi.ecommerce.product.PurchaseRequest;
import com.syamsandi.ecommerce.product.PurchaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    public Integer createOrder(OrderRequest request) {

        // check the customer --> OpenFeign
        CustomerResponse customerResponse = customerClient.findById(request.customerId()).orElseThrow(
                () -> new BusinessException("Cannot create order:: no Customer exists with the provided ID"));
        // purchase the product --> product-ms(RestTemplate)
        List<PurchaseResponse> purchaseResponses = productClient.purchaseProducts(request.products());
        // persist order
        Order savedOrder = repository.save(mapper.toOrder(request));
        // persis the order lines
        for(PurchaseRequest purchaseRequest: request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            savedOrder.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        // todo start payment process
        // send the order confirmation --> notification-ms (kafka)

        return null;
    }
}
