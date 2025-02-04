package com.example.mate.product.domain.event;

import com.example.mate.product.domain.Product;

public record ProductCreateEvent(
        Product product,
        Long custromerId
) {
}
