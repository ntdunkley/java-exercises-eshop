package com.nathan.exercise.eshop;

import com.nathan.exercise.eshop.domain.dto.requests.ModifyCartRequest;
import com.nathan.exercise.eshop.domain.dto.requests.ProductCreateRequest;
import com.nathan.exercise.eshop.domain.dto.responses.CartCheckoutResponse;
import com.nathan.exercise.eshop.domain.dto.responses.CartResponse;
import com.nathan.exercise.eshop.domain.dto.responses.ProductCartResponse;
import com.nathan.exercise.eshop.domain.dto.responses.ProductResponse;
import com.nathan.exercise.eshop.domain.types.ProductLabel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CartAndProductIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void endToEndTest() {
        ProductResponse product1 = createProduct("product1", 2.5f);
        ProductResponse product2 = createProduct("product2", 5f);
        CartResponse cart1 = createCart();
        addProductToCart(cart1.cartId(), product1.productId(), 2);
        addProductToCart(cart1.cartId(), product2.productId(), 1);

        CartCheckoutResponse cartCheckoutResponse = checkoutCart(cart1.cartId());

        // Assert product fields
        assertThat(cartCheckoutResponse.cart().products()).extracting(
                ProductCartResponse::productId,
                ProductCartResponse::quantity
        ).containsExactlyInAnyOrder(
                tuple(product1.productId(), 2),
                tuple(product2.productId(), 1)
        );
        // Assert cart fields
        assertThat(cartCheckoutResponse.cart()).extracting(
                CartResponse::cartId,
                CartResponse::checkedOut
        ).containsExactly(
                cart1.cartId(),
                true
        );
        // Assert total cost
        assertThat(cartCheckoutResponse.totalCost()).isEqualTo(10f);
    }

    private ProductResponse createProduct(String name, float price) {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(
                name, price, List.of(ProductLabel.FOOD)
        );
        return restTemplate.postForObject("http://localhost:" + port + "/products", productCreateRequest, ProductResponse.class);
    }

    private CartResponse createCart() {
        return restTemplate.postForObject("http://localhost:" + port + "/carts", null, CartResponse.class);
    }

    private void addProductToCart(Long cartId, Long productId, Integer quantity) {
        Map<String, Long> pathVars = Map.of("id", cartId);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest(productId, quantity);
        restTemplate.put("http://localhost:" + port + "/carts/{id}", List.of(modifyCartRequest), pathVars);
    }

    private CartCheckoutResponse checkoutCart(Long cartId) {
        Map<String, Long> pathVars = Map.of("id", cartId);
        return restTemplate.postForObject("http://localhost:" + port + "/carts/{id}/checkout", null, CartCheckoutResponse.class, pathVars);
    }
}
