package com.example.apibank.dtos;

import com.example.apibank.entities.AccountModel;

public class TransferDto {
    AccountModel origin;
    AccountModel destination;

    public TransferDto() {
    }

    public TransferDto(AccountModel origin, AccountModel destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public AccountModel getOrigin() {
        return origin;
    }

    public void setOrigin(AccountModel origin) {
        this.origin = origin;
    }

    public AccountModel getDestination() {
        return destination;
    }

    public void setDestination(AccountModel destination) {
        this.destination = destination;
    }
}
