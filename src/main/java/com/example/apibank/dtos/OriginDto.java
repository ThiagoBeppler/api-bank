package com.example.apibank.dtos;

import com.example.apibank.entities.AccountModel;

public class OriginDto {
    AccountModel origin;

    public OriginDto() {
    }

    public OriginDto(AccountModel origin) {
        this.origin = origin;
    }

    public AccountModel getOrigin() {
        return origin;
    }

    public void setOrigin(AccountModel origin) {
        this.origin = origin;
    }
}
