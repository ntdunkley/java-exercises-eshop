package com.nathan.exercise.eshop.domain.dto.responses;

import java.util.List;

public record CartResponse(
        Long cartId,
        List<ProductCartResponse> products,
        Boolean checkedOut
) {
}
