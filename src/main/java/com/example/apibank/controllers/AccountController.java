package com.example.apibank.controllers;

import com.example.apibank.dtos.EventDto;
import com.example.apibank.dtos.OriginDto;
import com.example.apibank.dtos.TransferDto;
import com.example.apibank.entities.AccountModel;
import com.example.apibank.interfaces.AccountService;
import com.example.apibank.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @GetMapping("teste1")
    public AccountModel teste(){

        return accountRepository.save(new AccountModel("12",12.0F));
    }

    @GetMapping("teste2")
    public OriginDto teste2(){

        AccountModel account = new AccountModel("12", 12.0F);

        return new OriginDto(account);
    }

    @GetMapping("teste3")
    public TransferDto teste3(){

        AccountModel account = new AccountModel("100", 0F);
        AccountModel account2 = new AccountModel("300", 15F);

        return new TransferDto(account,account2);
    }

    @GetMapping("balance")
    public Float balance(@RequestParam(name = "account_id") String id){

        return accountService.balance(id);
    }

    @PostMapping("event")
    @ResponseStatus(HttpStatus.CREATED)
    public Object event(@RequestBody EventDto event){

        return accountService.transferEvent(event);
    }

    @PostMapping("reset")
    public HttpStatus reset(){

        accountService.reset();

        return HttpStatus.OK;
    }
}
