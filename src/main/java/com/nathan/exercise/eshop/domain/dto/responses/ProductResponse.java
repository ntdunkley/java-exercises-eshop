package com.nathan.exercise.eshop.domain.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nathan.exercise.eshop.domain.types.ProductLabel;

import java.time.LocalDate;
import java.util.List;

public record ProductResponse(
        long productId,
        String name,
        Float price,
        @JsonFormat(pattern = "yyyy/MM/dd") LocalDate addedAt,
        List<ProductLabel> labels
) {
}
