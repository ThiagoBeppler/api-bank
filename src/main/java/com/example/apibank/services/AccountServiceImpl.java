package com.example.apibank.services;

import com.example.apibank.dtos.EventDto;
import com.example.apibank.dtos.TransferDto;
import com.example.apibank.entities.AccountModel;
import com.example.apibank.interfaces.AccountService;
import com.example.apibank.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Float balance(String id){

         Optional<AccountModel> account = accountRepository.findById(id);

         if(account.isPresent())
             return account.get().getBalance();
         else
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!");

     }

    @Override
    public Object transferEvent(EventDto event){

        switch (event.getType()){
            case "deposit":
                return deposit(event);
            case "withdraw":
                return withdraw(event);
            case "transfer":
                AccountModel deposit = deposit(event);
                AccountModel withdraw = withdraw(event);
                return new TransferDto(withdraw, deposit);
        }

        return new AccountModel();
    }

    @Override
    public void reset(){

        accountRepository.deleteAll();
    }

    private AccountModel deposit(EventDto event){

        Optional<AccountModel> account = accountRepository.findById(event.getDestination());

        if(account.isPresent()){

            String id = account.get().getId();
            Float balance = account.get().getBalance();
            balance += event.getAmount();

            return accountRepository.save(new AccountModel(id, balance));
        }
        else
            return accountRepository.save(new AccountModel(event.getDestination(), event.getAmount()));
    }

    private AccountModel withdraw(EventDto event){

        Optional<AccountModel> account = accountRepository.findById(event.getOrigin());

        if(account.isPresent()){

            String id = account.get().getId();
            Float balance = account.get().getBalance();
            balance -= event.getAmount();

            return accountRepository.save(new AccountModel(id, balance));
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!");
    }
}
