package com.example.smartfood.Models;

import androidx.annotation.NonNull;

import com.example.smartfood.Constants.Status;

import java.util.UUID;

public class Order {
    public Order(UUID id, UUID customerId, User customer, int count, UUID orderedDishId, StoredDish orderedDish, double totalPrice, Status status) {
        this.id = id;
        this.customerId = customerId;
        this.customer = customer;
        this.count = count;
        this.orderedDishId = orderedDishId;
        this.orderedDish = orderedDish;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Order(UUID customerId, int count, UUID orderedDishId, double totalPrice) {
        this.customerId = customerId;
        this.count = count;
        this.orderedDishId = orderedDishId;
        this.totalPrice = totalPrice;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UUID getOrderedDishId() {
        return orderedDishId;
    }

    public void setOrderedDishId(UUID orderedDishId) {
        this.orderedDishId = orderedDishId;
    }

    public StoredDish getOrderedDish() {
        return orderedDish;
    }

    public void setOrderedDish(StoredDish orderedDish) {
        this.orderedDish = orderedDish;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private UUID id;
    private UUID customerId;
    private User customer;
    private int count;
    private UUID orderedDishId;
    private StoredDish orderedDish;
    private double totalPrice;
    private Status status;

    @NonNull
    @Override
    public String toString() {
        return orderedDish.getDish().getName()
                + "\n" + orderedDish.getDish().getPrice()
                + " x " + count
                + " = " + totalPrice
                + "\n(" + orderedDish.getFridge().getPlacementDescription() + ")";
    }
}
