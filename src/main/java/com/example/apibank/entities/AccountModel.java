package com.example.apibank.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "Account")
public class AccountModel {
    @Id
    private String id;
    private Float balance;

    public AccountModel() {
    }

    public AccountModel(String id, Float balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }
}
