package com.example.apibank.controllers;

import com.example.apibank.dtos.EventDto;
import com.example.apibank.interfaces.AccountService;
import com.example.apibank.interfaces.EventResponse;
import com.example.apibank.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(accountService.balance(id));
    }

    @PostMapping("/event")
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse event(@RequestBody EventDto event) {
        return accountService.transferEvent(event);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset() {
        accountService.reset();
        return ResponseEntity.ok("OK");
    }
}
