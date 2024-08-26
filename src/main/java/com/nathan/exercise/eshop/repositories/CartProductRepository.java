package com.nathan.exercise.eshop.repositories;

import com.nathan.exercise.eshop.entities.CartEntity;
import com.nathan.exercise.eshop.entities.CartProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProductEntity, Long> {
    List<CartProductEntity> findByCart(CartEntity cart);
}
