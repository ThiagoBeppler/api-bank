package com.example.apibank.dtos;

import java.math.BigDecimal;

public class EventDto {
    private String type;
    private String origin;
    private BigDecimal amount;
    private String destination;

    public EventDto() {
    }

    public EventDto(String type, String origin, BigDecimal amount, String destination) {
        this.type = type;
        this.origin = origin;
        this.amount = amount;
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
