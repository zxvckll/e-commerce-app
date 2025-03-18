package com.syamsandi.ecommerce.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    public Integer createProduct(@Valid ProductRequest request) {
        return null;
    }

    public List<ProductPurhaseResponse> purchaseProduct(List<ProductPurchaseRequest> request) {
        return null;
    }
}
