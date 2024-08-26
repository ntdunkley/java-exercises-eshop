package com.nathan.exercise.eshop.domain.dto.responses;

public record ProductCartResponse(
        Long productId,
        Integer quantity
) {
}
