package com.nathan.exercise.eshop.domain.dto.requests;

public record ModifyCartRequest(
        Long productId,
        Integer quantity
) {
}
