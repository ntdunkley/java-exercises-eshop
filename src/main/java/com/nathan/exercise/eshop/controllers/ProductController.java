package com.nathan.exercise.eshop.controllers;

import com.nathan.exercise.eshop.domain.dto.requests.ProductCreateRequest;
import com.nathan.exercise.eshop.domain.dto.responses.ProductResponse;
import com.nathan.exercise.eshop.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public @ResponseStatus(HttpStatus.CREATED) ProductResponse addProduct(@Valid @RequestBody ProductCreateRequest productRequest) {
        return productService.addProduct(productRequest);
    }

    @DeleteMapping("/{id}")
    public @ResponseStatus(HttpStatus.NO_CONTENT) void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping
    public List<ProductResponse> listProducts() {
        return productService.listProducts();
    }
}
