package com.nathan.exercise.eshop.services;

import com.nathan.exercise.eshop.domain.dto.requests.ProductCreateRequest;
import com.nathan.exercise.eshop.domain.dto.responses.ProductResponse;
import com.nathan.exercise.eshop.entities.ProductEntity;
import com.nathan.exercise.eshop.exceptions.ResourceExistsException;
import com.nathan.exercise.eshop.exceptions.ResourceNotFoundException;
import com.nathan.exercise.eshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse addProduct(ProductCreateRequest productRequest) {
        productRepository
                .findByName(productRequest.name())
                .ifPresent(o -> {
                    throw new ResourceExistsException("Product with name " + o.getName() + " already exists");
                });

        ProductEntity product = new ProductEntity(
                productRequest.name(),
                productRequest.price(),
                LocalDate.now(),
                productRequest.labels()
        );
        ProductEntity createdProduct = productRepository.save(product);
        return mapResponse(createdProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductResponse getProduct(Long id) {
        return productRepository
                .findById(id)
                .map(this::mapResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with id = " + id));
    }

    public List<ProductResponse> listProducts() {
        List<ProductResponse> products = productRepository
                .findAll()
                .stream()
                .map(this::mapResponse)
                .toList();
        return products;
    }

    private ProductResponse mapResponse(ProductEntity product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getAddedAt(),
                product.getLabels()
        );
    }
}
