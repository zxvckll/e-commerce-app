package com.syamsandi.ecommerce.product;

import com.syamsandi.ecommerce.category.Category;
import com.syamsandi.ecommerce.category.CategoryRepository;
import com.syamsandi.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    public Integer createProduct(@Valid ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.categoryId()));

        Product product = mapper.toProduct(request,category);
        return repository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProduct(List<ProductPurchaseRequest> request) {
        List<Integer> products_id = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        List<Product> storedProducts = repository.findAllByIdInOrderById(products_id);
        if (products_id.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exist");
        }
        List<ProductPurchaseRequest> requests = request.stream().sorted(Comparator.comparing(ProductPurchaseRequest::productId)).toList();
        ArrayList<ProductPurchaseResponse> responses = new ArrayList<>();
        for (int i=0; i<storedProducts.size(); i++) {
            Product product = storedProducts.get(i);
            ProductPurchaseRequest purchaseRequest = requests.get(i);
            if (product.getAvailableQuantity() < purchaseRequest.quantity()){
                throw new ProductPurchaseException("Insufficient stock quantity for product with ID:: " + purchaseRequest.productId());
            }
            Double newAvailableQuantity = product.getAvailableQuantity() - purchaseRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);
            responses.add(mapper.toProductPurchaseResponse(product,purchaseRequest.quantity()));
        }
        return responses;
    }

    public ProductResponse findById(Integer productId) {
        return repository.findById(productId).map(mapper::toProductResponse).orElseThrow(
                () -> new EntityNotFoundException("Product not found with the ID:: " + productId)
        );
    }

    public List<ProductResponse> findAll() {
        return repository.findAll().stream().map(mapper::toProductResponse).collect(Collectors.toList());
    }
}
