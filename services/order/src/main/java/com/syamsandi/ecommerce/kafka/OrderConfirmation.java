package com.syamsandi.ecommerce.kafka;

import com.syamsandi.ecommerce.customer.CustomerResponse;
import com.syamsandi.ecommerce.order.PaymentMethod;
import com.syamsandi.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> product
) {
}
