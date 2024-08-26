package com.nathan.exercise.eshop.controllers;

import com.nathan.exercise.eshop.domain.dto.requests.ModifyCartRequest;
import com.nathan.exercise.eshop.domain.dto.responses.CartCheckoutResponse;
import com.nathan.exercise.eshop.domain.dto.responses.CartResponse;
import com.nathan.exercise.eshop.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public @ResponseStatus(HttpStatus.CREATED) CartResponse createCart() {
        return cartService.createCart();
    }

    @PutMapping("/{id}")
    public CartResponse modifyCart(@PathVariable Long id, @RequestBody List<ModifyCartRequest> modifyCartRequests) {
        return cartService.modifyCart(id, modifyCartRequests);
    }

    @GetMapping
    public List<CartResponse> listCarts() {
        return cartService.listCarts();
    }

    @PostMapping("/{id}/checkout")
    public CartCheckoutResponse checkoutCart(@PathVariable Long id) {
        return cartService.checkoutCart(id);
    }
}
