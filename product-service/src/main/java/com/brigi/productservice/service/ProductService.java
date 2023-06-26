package com.brigi.productservice.service;

import com.brigi.productservice.dto.ProductRequest;
import com.brigi.productservice.dto.ProductResponse;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    void createProduct(final ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    boolean updateProduct(final ProductResponse productResponse);
}
