package com.nathan.exercise.eshop.domain.dto.responses;

public record CartCheckoutResponse(
        CartResponse cart,
        Float totalCost
) {
}
