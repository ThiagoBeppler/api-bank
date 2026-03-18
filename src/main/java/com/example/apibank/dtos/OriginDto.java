package com.example.apibank.dtos;

import com.example.apibank.entities.AccountModel;
import com.example.apibank.interfaces.EventResponse;

public class OriginDto implements EventResponse {
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
