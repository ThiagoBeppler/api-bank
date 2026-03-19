package com.example.apibank.services;

import com.example.apibank.dtos.DestinationDto;
import com.example.apibank.dtos.EventDto;
import com.example.apibank.dtos.OriginDto;
import com.example.apibank.dtos.TransferDto;
import com.example.apibank.entities.AccountModel;
import com.example.apibank.exceptions.AccountNotFoundException;
import com.example.apibank.interfaces.AccountService;
import com.example.apibank.interfaces.EventResponse;
import com.example.apibank.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public BigDecimal balance(String id){

         Optional<AccountModel> account = accountRepository.findById(id);

        return account.map(AccountModel::getBalance)
                .orElseThrow(() -> new AccountNotFoundException("Account not found!"));
     }

    @Override
    @Transactional
    public EventResponse transferEvent(EventDto event){

        return switch (event.getType()) {
            case DEPOSIT -> deposit(event);
            case WITHDRAW -> withdraw(event);
            case TRANSFER -> transfer(event);
        };
    }

    @Override
    public void reset(){

        accountRepository.deleteAll();
    }

    private EventResponse deposit(EventDto event){

        AccountModel account = accountRepository.findById(event.getDestination())
                .orElseGet(() -> new AccountModel(event.getDestination()));

        account.credit(event.getAmount());


        return new DestinationDto(accountRepository.save(account));
    }

    private EventResponse withdraw(EventDto event){

        AccountModel account = accountRepository.findById(event.getOrigin())
                .orElseThrow(() -> new AccountNotFoundException("Account not found!"));

        account.debit(event.getAmount());

        return new OriginDto(accountRepository.save(account));
    }

    public TransferDto transfer(EventDto event) {

        AccountModel origin = accountRepository.findById(event.getOrigin())
                .orElseThrow(() -> new AccountNotFoundException("Account not found!"));

        AccountModel destination = accountRepository.findById(event.getDestination())
                .orElseGet(() -> new AccountModel(event.getDestination()));

        BigDecimal amount = event.getAmount();

        origin.debit(amount);
        destination.credit(amount);

        accountRepository.saveAll(List.of(origin, destination));

        return new TransferDto(origin, destination);
    }
}
