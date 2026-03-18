package com.example.apibank.interfaces;

import com.example.apibank.dtos.EventDto;

import java.math.BigDecimal;

public interface AccountService {

    public abstract EventResponse transferEvent(EventDto event);

    public abstract BigDecimal balance(String id);

    public abstract void reset();

}
