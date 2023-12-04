package com.example.smartfood.Models;

import androidx.annotation.NonNull;

import java.util.UUID;

public class StoredDish {
    public StoredDish(UUID id, int countAvailable, UUID dishId, Dish dish, UUID fridgeId, Fridge fridge) {
        this.id = id;
        this.countAvailable = countAvailable;
        this.dishId = dishId;
        this.dish = dish;
        this.fridgeId = fridgeId;
        this.fridge = fridge;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getCountAvailable() {
        return countAvailable;
    }

    public void setCountAvailable(int countAvailable) {
        this.countAvailable = countAvailable;
    }

    public UUID getDishId() {
        return dishId;
    }

    public void setDishId(UUID dishId) {
        this.dishId = dishId;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public UUID getFridgeId() {
        return fridgeId;
    }

    public void setFridgeId(UUID fridgeId) {
        this.fridgeId = fridgeId;
    }

    public Fridge getFridge() {
        return fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }

    @NonNull
    @Override
    public String toString() {
        return getDish().getName() + ", " + getFridge().getPlacementDescription();
    }

    public String toStringForSupplier() {
        return getDish().getName()
                + "\n(" + getFridge().getFilial().getName()
                + ", " + getFridge().getFilial().getAddress() + ")";
    }

    private UUID id;
    private int countAvailable;
    private UUID dishId;
    private Dish dish;
    private UUID fridgeId;
    private Fridge fridge;
}
