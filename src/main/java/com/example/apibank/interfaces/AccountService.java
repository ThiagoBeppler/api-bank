package com.example.apibank.interfaces;

import com.example.apibank.dtos.EventDto;
import com.example.apibank.entities.AccountModel;

public interface AccountService {

    public abstract Object transferEvent(EventDto event);

    public abstract Float balance(String id);

}
