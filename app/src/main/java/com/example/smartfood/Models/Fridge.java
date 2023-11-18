package com.example.smartfood.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Fridge {
    private UUID id;
    private String placementDescription = "";
    private boolean isOpen;
    private boolean isDeleted = false;
    private String uri = "";
    private UUID filialId;
    private Filial filial;
    private List<StoredDish> dishesServed;

    public Fridge(UUID id, String placementDescription, boolean isOpen, boolean isDeleted, String uri, UUID filialId, Filial filial, List<StoredDish> dishesServed) {
        this.id = id;
        this.placementDescription = placementDescription;
        this.isOpen = isOpen;
        this.isDeleted = isDeleted;
        this.uri = uri;
        this.filialId = filialId;
        this.filial = filial;
        this.dishesServed = dishesServed;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlacementDescription() {
        return placementDescription;
    }

    public void setPlacementDescription(String placementDescription) {
        this.placementDescription = placementDescription;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getURI() {
        return uri;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public UUID getFilialId() {
        return filialId;
    }

    public void setFilialId(UUID filialId) {
        this.filialId = filialId;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public List<StoredDish> getDishesServed() {
        return dishesServed;
    }

    public void setDishesServed(List<StoredDish> dishesServed) {
        this.dishesServed = dishesServed;
    }
}
