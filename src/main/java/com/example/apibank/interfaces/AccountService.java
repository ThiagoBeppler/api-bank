package com.example.apibank.interfaces;

import com.example.apibank.dtos.EventDto;

public interface AccountService {

    public abstract Object transferEvent(EventDto event);

    public abstract Float balance(String id);

    public abstract void reset();

}
