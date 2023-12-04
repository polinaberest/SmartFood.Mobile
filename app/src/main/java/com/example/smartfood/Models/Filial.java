package com.example.smartfood.Models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Filial {
    private UUID id;
    private String name = "";
    private String address = "";
    private UUID organizationId;
    private Organization ownerOrganization;
    private List<Fridge> fridgesInstalled;
    private boolean isDeleted = false;

    public Filial(UUID id, String name, String address, UUID organizationId, Organization ownerOrganization, List<Fridge> fridgesInstalled, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.organizationId = organizationId;
        this.ownerOrganization = ownerOrganization;
        this.fridgesInstalled = fridgesInstalled;
        this.isDeleted = isDeleted;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public Organization getOwnerOrganization() {
        return ownerOrganization;
    }

    public void setOwnerOrganization(Organization ownerOrganization) {
        this.ownerOrganization = ownerOrganization;
    }

    public List<Fridge> getFridgesInstalled() {
        return fridgesInstalled;
    }

    public void setFridgesInstalled(List<Fridge> fridgesInstalled) {
        this.fridgesInstalled = fridgesInstalled;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + ", " + getOwnerOrganization().getName();
    }
}
