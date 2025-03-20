package com.syamsandi.ecommerce.order;

import com.syamsandi.ecommerce.customer.CustomerClient;
import com.syamsandi.ecommerce.customer.CustomerResponse;
import com.syamsandi.ecommerce.exception.BusinessException;
import com.syamsandi.ecommerce.kafka.OrderConfirmation;
import com.syamsandi.ecommerce.kafka.OrderProducer;
import com.syamsandi.ecommerce.orderline.OrderLineRequest;
import com.syamsandi.ecommerce.orderline.OrderLineService;
import com.syamsandi.ecommerce.payment.PaymentClient;
import com.syamsandi.ecommerce.payment.PaymentRequest;
import com.syamsandi.ecommerce.product.ProductClient;
import com.syamsandi.ecommerce.product.PurchaseRequest;
import com.syamsandi.ecommerce.product.PurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
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
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;
    public Integer createOrder(OrderRequest request) {

        // check the customer --> OpenFeign
        CustomerResponse customer = customerClient.findById(request.customerId()).orElseThrow(
                () -> new BusinessException("Cannot create order:: no Customer exists with the provided ID"));
        // purchase the product --> product-ms(RestTemplate)
        List<PurchaseResponse> purchasedProducts = productClient.purchaseProducts(request.products());
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
        // start payment process
        paymentClient.requestOrderPayment(new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                savedOrder.getId(),
                savedOrder.getReference(),
                customer
        ));

        // send the order confirmation --> notification-ms (kafka)
        orderProducer.orderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return savedOrder.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll().stream().map(mapper::fromOrder).toList();
    }

    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId).map(mapper::fromOrder).orElseThrow(
                () -> new EntityNotFoundException(String.format("No order found with ID %d", orderId))
        );
    }
}
