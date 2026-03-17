package com.example.apibank;

import com.example.apibank.dtos.EventDto;
import com.example.apibank.entities.AccountModel;
import com.example.apibank.repositories.AccountRepository;
import com.example.apibank.services.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBalance_AccountExists() {
        String accountId = "123";
        AccountModel account = new AccountModel(accountId, 100.0f);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Float balance = accountService.balance(accountId);

        assertEquals(100.0f, balance);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testBalance_AccountNotFound() {
        String accountId = "123";
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> accountService.balance(accountId));

        assertEquals("404 NOT_FOUND \"Account not found!\"", exception.getMessage());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testTransferEvent_Deposit() {
        EventDto event = new EventDto("deposit", null, 50.0f, "123");
        AccountModel account = new AccountModel("123", 100.0f);
        when(accountRepository.findById("123")).thenReturn(Optional.of(account));
        when(accountRepository.save(any(AccountModel.class))).thenReturn(new AccountModel("123", 150.0f));

        Object result = accountService.transferEvent(event);

        assertNotNull(result);
        verify(accountRepository, times(1)).findById("123");
        verify(accountRepository, times(1)).save(any(AccountModel.class));
    }

    @Test
    void testTransferEvent_Withdraw() {
        EventDto event = new EventDto("withdraw", "123", 50.0f, null);
        AccountModel account = new AccountModel("123", 100.0f);
        when(accountRepository.findById("123")).thenReturn(Optional.of(account));
        when(accountRepository.save(any(AccountModel.class))).thenReturn(new AccountModel("123", 50.0f));

        Object result = accountService.transferEvent(event);

        assertNotNull(result);
        verify(accountRepository, times(1)).findById("123");
        verify(accountRepository, times(1)).save(any(AccountModel.class));
    }

    @Test
    void testTransferEvent_Transfer() {
        EventDto event = new EventDto("transfer", "123", 50.0f, "456");
        AccountModel originAccount = new AccountModel("123", 100.0f);
        AccountModel destinationAccount = new AccountModel("456", 200.0f);
        when(accountRepository.findById("123")).thenReturn(Optional.of(originAccount));
        when(accountRepository.findById("456")).thenReturn(Optional.of(destinationAccount));
        when(accountRepository.save(any(AccountModel.class))).thenReturn(new AccountModel("123", 50.0f), new AccountModel("456", 250.0f));

        Object result = accountService.transferEvent(event);

        assertNotNull(result);
        verify(accountRepository, times(2)).findById(anyString());
        verify(accountRepository, times(2)).save(any(AccountModel.class));
    }

    @Test
    void testReset() {
        accountService.reset();

        verify(accountRepository, times(1)).deleteAll();
    }
}