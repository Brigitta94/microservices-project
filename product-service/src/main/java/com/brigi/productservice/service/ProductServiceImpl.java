package com.brigi.productservice.service;

import com.brigi.productservice.dto.ProductRequest;
import com.brigi.productservice.dto.ProductResponse;
import com.brigi.productservice.entity.Product;
import com.brigi.productservice.repository.ProductRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Observed(name = "productService")
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .description(productRequest.description())
                .name(productRequest.name())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    public boolean updateProduct(ProductResponse productResponse) {
        return productRepository.findById(productResponse.id())
                .map(p -> {
                    productRepository.save(Product.builder().id(productResponse.id()).build());
                return true;})
                .orElse(false);
    }

    private ProductResponse mapToProductResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .build();
    }
}
