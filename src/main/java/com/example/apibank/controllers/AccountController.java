package com.example.apibank.controllers;

import com.example.apibank.dtos.EventDto;
import com.example.apibank.interfaces.AccountService;
import com.example.apibank.interfaces.EventResponse;
import com.example.apibank.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> balance(@RequestParam(name = "account_id") String id){

        BigDecimal balance = accountService.balance(id);

        if (balance == null)
            return ResponseEntity.status(404).body(new BigDecimal("0"));

        return ResponseEntity.ok(balance);
    }

    @PostMapping("/event")
    public ResponseEntity<?> event(@RequestBody EventDto event) {

        try {
            EventResponse response = accountService.transferEvent(event);
            return ResponseEntity.status(201).body(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(404).body(new BigDecimal("0"));
        }

    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset() {
        accountService.reset();
        return ResponseEntity.ok("OK");
    }
}
