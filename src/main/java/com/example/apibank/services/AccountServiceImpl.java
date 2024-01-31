package com.example.apibank.services;

import com.example.apibank.dtos.EventDto;
import com.example.apibank.dtos.TransferDto;
import com.example.apibank.entities.AccountModel;
import com.example.apibank.interfaces.AccountService;
import com.example.apibank.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    public AccountModel transferEvent(EventDto event){

        switch (event.getType()){
            case "deposit":
                return deposit(event);
        }

        return new AccountModel();
    }

    private AccountModel deposit(EventDto event){

        Optional<AccountModel> account= accountRepository.findById(event.getDestination());

        if(account.isPresent()){

            String id = account.get().getId();
            Float balance = account.get().getBalance();
            balance += event.getAmount();

            return (accountRepository.save(new AccountModel(id, balance)));
        }
        else
            return (accountRepository.save(new AccountModel(event.getDestination(), event.getAmount())));
    }
}
