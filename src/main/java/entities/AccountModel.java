package entities;

import jakarta.persistence.Entity;

@Entity(name = "Account")
public class AccountModel {
    private Integer id;
    private Float balance;

    public AccountModel() {
    }

    public AccountModel(Integer id, Float balance) {
        this.id = id;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }
}
