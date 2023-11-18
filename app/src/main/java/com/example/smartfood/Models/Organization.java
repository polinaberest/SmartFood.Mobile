package com.example.smartfood.Models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Organization {
    private UUID id;
    private boolean isBlocked;
    private String name = "";
    private String description;
    private Date registerDate;
    private UUID managerId;
    private User manager;
    private List<Filial> filials;

    public Organization(UUID id, boolean isBlocked, String name, String description, Date registerDate, UUID managerId, User manager, List<Filial> filials) {
        this.id = id;
        this.isBlocked = isBlocked;
        this.name = name;
        this.description = description;
        this.registerDate = registerDate;
        this.managerId = managerId;
        this.manager = manager;
        this.filials = filials;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public UUID getManagerId() {
        return managerId;
    }

    public void setManagerId(UUID managerId) {
        this.managerId = managerId;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public List<Filial> getFilials() {
        return filials;
    }

    public void setFilials(List<Filial> filials) {
        this.filials = filials;
    }
}
