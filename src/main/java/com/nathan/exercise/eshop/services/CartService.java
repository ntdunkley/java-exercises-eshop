package com.nathan.exercise.eshop.services;

import com.nathan.exercise.eshop.domain.dto.requests.ModifyCartRequest;
import com.nathan.exercise.eshop.domain.dto.responses.CartCheckoutResponse;
import com.nathan.exercise.eshop.domain.dto.responses.CartResponse;
import com.nathan.exercise.eshop.domain.dto.responses.ProductCartResponse;
import com.nathan.exercise.eshop.entities.CartEntity;
import com.nathan.exercise.eshop.entities.CartProductEntity;
import com.nathan.exercise.eshop.entities.ProductEntity;
import com.nathan.exercise.eshop.exceptions.CartAlreadyCheckedOutException;
import com.nathan.exercise.eshop.exceptions.ResourceNotFoundException;
import com.nathan.exercise.eshop.repositories.CartProductRepository;
import com.nathan.exercise.eshop.repositories.CartRepository;
import com.nathan.exercise.eshop.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository,
            CartProductRepository cartProductRepository
    ) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartProductRepository = cartProductRepository;
    }

    public CartResponse createCart() {
        CartEntity cart = new CartEntity(false);
        Long cartId = cartRepository.save(cart).getId();
        return new CartResponse(cartId, List.of(), false);
    }

    @Transactional
    public CartResponse modifyCart(Long cartId, List<ModifyCartRequest> modifyCartRequests) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        if (cart.getCheckedOut()) {
            throw new CartAlreadyCheckedOutException("Cart cannot be modified");
        }

        List<CartProductEntity> existingCartProducts = cartProductRepository.findByCart(cart);
        Map<Long, CartProductEntity> existingProductMap = existingCartProducts
                .stream()
                .collect(Collectors.toMap(cp -> cp.getProduct().getId(), Function.identity()));

        List<CartProductEntity> toSaveOrUpdate = new ArrayList<>();
        for (ModifyCartRequest request : modifyCartRequests) {
            ProductEntity product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            // Attempt to retrieve from cache
            CartProductEntity cartProduct = existingProductMap.get(product.getId());
            if (cartProduct == null) {
                // Create new entity
                cartProduct = new CartProductEntity(cart, product, request.quantity());
            } else {
                // Update entity if exists
                cartProduct.setQuantity(request.quantity());
            }
            toSaveOrUpdate.add(cartProduct);
        }
        cartProductRepository.saveAll(toSaveOrUpdate);

        // We know the cart exists now so call get()
        return mapResponse(cartRepository.findById(cartId).get());
    }

    public List<CartResponse> listCarts() {
        return cartRepository
                .findAll()
                .stream()
                .map(this::mapResponse)
                .toList();
    }

    public CartCheckoutResponse checkoutCart(Long cartId) {
        CartEntity cart = cartRepository
                .findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartProductEntity> cartProductEntities = cartProductRepository.findByCart(cart);
        Float cartTotalCost = 0f;
        for (CartProductEntity cartProduct : cartProductEntities) {
            cartTotalCost += cartProduct.getProduct().getPrice() * cartProduct.getQuantity();
        }

        cart.setCheckedOut(true);
        CartEntity updatedCart = cartRepository.save(cart);
        return new CartCheckoutResponse(mapResponse(updatedCart), cartTotalCost);
    }

    private CartResponse mapResponse(CartEntity cartEntity) {
        List<CartProductEntity> cartProducts = cartProductRepository.findByCart(cartEntity);

        List<ProductCartResponse> products = cartProducts
                .stream()
                .map(cp -> new ProductCartResponse(cp.getProduct().getId(), cp.getQuantity()))
                .toList();

        return new CartResponse(
                cartEntity.getId(),
                products,
                cartEntity.getCheckedOut()
        );
    }
}
