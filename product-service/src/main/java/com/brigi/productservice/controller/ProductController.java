package com.brigi.productservice.controller;

import com.brigi.productservice.dto.ProductRequest;
import com.brigi.productservice.dto.ProductResponse;
import com.brigi.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public boolean updateProduct(@RequestBody ProductResponse productResponse) {
        return productService.updateProduct(productResponse);
    }
}
