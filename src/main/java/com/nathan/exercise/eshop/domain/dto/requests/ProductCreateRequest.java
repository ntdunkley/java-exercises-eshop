package com.nathan.exercise.eshop.domain.dto.requests;

import com.nathan.exercise.eshop.domain.types.ProductLabel;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record ProductCreateRequest(
        @Length(max = 200) String name,
        Float price,
        List<ProductLabel> labels
) {
}
