package com.example.apibank.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.annotation.Version;

import java.math.BigDecimal;

@Entity
public class AccountModel {

    @Id
    private String id;

    private BigDecimal balance;

    @Version
    private Long version;

    protected AccountModel() {}

    public AccountModel(String id) {
        this.id = id;
        this.balance = BigDecimal.ZERO;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getId() {
        return id;
    }

    public void credit(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        validateAmount(amount);

        if (this.balance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        this.balance = this.balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new RuntimeException("Invalid amount");
        }
    }
}
