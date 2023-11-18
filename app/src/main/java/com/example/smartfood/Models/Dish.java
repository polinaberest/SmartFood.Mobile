package com.example.smartfood.Models;

import java.util.UUID;

public class Dish {
    public Dish(UUID id, String name, double price, String description, UUID supplierId, Supplier supplier) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.supplierId = supplierId;
        this.supplier = supplier;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    private UUID id;
    private String name;
    private double price;
    private String description;
    private UUID supplierId;
    private Supplier supplier;
}
