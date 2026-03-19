package com.example.apibank.enus;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum EventType {
    DEPOSIT("deposit"),
    WITHDRAW("withdraw"),
    TRANSFER("transfer");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static EventType from(String value) {
        return Arrays.stream(values())
                .filter(e -> e.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid event type"));
    }
}