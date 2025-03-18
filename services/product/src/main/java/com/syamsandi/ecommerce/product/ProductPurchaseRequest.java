package com.syamsandi.ecommerce.product;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductPurchaseRequest(
        @NotNull(message = "Product is mandatory")
        Integer productId,
        @NotNull(message = "Quantity is mandatory")
        Double quantity
) {

}
