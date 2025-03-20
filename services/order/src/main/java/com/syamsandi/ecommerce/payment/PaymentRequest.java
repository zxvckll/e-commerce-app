package com.syamsandi.ecommerce.payment;

import com.syamsandi.ecommerce.customer.CustomerResponse;
import com.syamsandi.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
