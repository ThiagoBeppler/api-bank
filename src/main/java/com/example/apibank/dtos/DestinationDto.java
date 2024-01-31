package com.example.apibank.dtos;

import com.example.apibank.entities.AccountModel;

public class DestinationDto {
    AccountModel destination;

    public DestinationDto() {
    }

    public DestinationDto(AccountModel destination) {
        this.destination = destination;
    }

    public AccountModel getDestination() {
        return destination;
    }

    public void setDestination(AccountModel destination) {
        this.destination = destination;
    }
}
