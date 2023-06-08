package com.brigi.productservice.service;

import com.brigi.productservice.dto.ProductRequest;
import com.brigi.productservice.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    void createProduct(final ProductRequest productRequest);

    List<ProductResponse> getAllProducts();
}
