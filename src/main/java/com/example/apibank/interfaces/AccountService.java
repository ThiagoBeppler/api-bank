package com.example.apibank.interfaces;

import com.example.apibank.dtos.EventDto;
import com.example.apibank.entities.AccountModel;

public interface AccountService {

    public AccountModel transferEvent(EventDto event);

}
