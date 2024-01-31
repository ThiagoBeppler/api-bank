package com.example.apibank.dtos;

public class EventDto {
    private String type;
    private String origin;
    private Float amount;
    private String destination;

    public EventDto() {
    }

    public EventDto(String type, String origin, Float amount, String destination) {
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
