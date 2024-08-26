package com.nathan.exercise.eshop.entities;

import com.nathan.exercise.eshop.domain.types.ProductLabel;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    private Float price;
    private LocalDate addedAt;

    @ElementCollection
    private List<ProductLabel> labels;

    protected ProductEntity() {

    }

    public ProductEntity(String name, Float price, LocalDate addedAt, List<ProductLabel> labels) {
        this.name = name;
        this.price = price;
        this.addedAt = addedAt;
        this.labels = labels;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDate getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDate addedAt) {
        this.addedAt = addedAt;
    }

    public List<ProductLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ProductLabel> labels) {
        this.labels = labels;
    }
}
