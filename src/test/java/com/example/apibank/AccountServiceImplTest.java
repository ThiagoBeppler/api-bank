package com.example.apibank;

import com.example.apibank.dtos.EventDto;
import com.example.apibank.entities.AccountModel;
import com.example.apibank.enus.EventType;
import com.example.apibank.exceptions.AccountNotFoundException;
import com.example.apibank.repositories.AccountRepository;
import com.example.apibank.services.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
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
        AccountModel account = new AccountModel(accountId);
        account.credit((new BigDecimal("100.00")));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        BigDecimal balance = accountService.balance(accountId);

        assertEquals(new BigDecimal("100.00"), balance);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testBalance_AccountNotFound() {
        String accountId = "123";
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> accountService.balance(accountId));

        assertEquals("Account not found!", exception.getMessage());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void testTransferEvent_Deposit() {
        EventDto event = new EventDto(EventType.DEPOSIT, null, new BigDecimal("50.00"), "123");
        AccountModel account = new AccountModel("123");
        account.credit(new BigDecimal("100.00"));
        when(accountRepository.findById("123")).thenReturn(Optional.of(account));

        AccountModel mockAccount = new AccountModel("123");
        mockAccount.credit(new BigDecimal("150.00"));

        when(accountRepository.save(any(AccountModel.class))).thenReturn(mockAccount);

        Object result = accountService.transferEvent(event);

        assertNotNull(result);
        verify(accountRepository, times(1)).findById("123");
        verify(accountRepository, times(1)).save(any(AccountModel.class));
    }

    @Test
    void testTransferEvent_Withdraw() {
        EventDto event = new EventDto(EventType.WITHDRAW, "123", new BigDecimal("50.00"), null);
        AccountModel account = new AccountModel("123");
        account.credit(new BigDecimal("100.00"));
        when(accountRepository.findById("123")).thenReturn(Optional.of(account));

        AccountModel mockAccount = new AccountModel("123");
        mockAccount.credit(new BigDecimal("50.00"));

        when(accountRepository.save(any(AccountModel.class))).thenReturn(mockAccount);

        Object result = accountService.transferEvent(event);

        assertNotNull(result);
        verify(accountRepository, times(1)).findById("123");
        verify(accountRepository, times(1)).save(any(AccountModel.class));
    }

    @Test
    void testTransferEvent_Transfer() {
        EventDto event = new EventDto(EventType.TRANSFER, "123", new BigDecimal("50.00"), "456");
        AccountModel originAccount = new AccountModel("123");
        originAccount.credit(new BigDecimal("100.00"));
        AccountModel destinationAccount = new AccountModel("456");
        destinationAccount.credit(new BigDecimal("200.00"));

        when(accountRepository.findById("123")).thenReturn(Optional.of(originAccount));
        when(accountRepository.findById("456")).thenReturn(Optional.of(destinationAccount));

        AccountModel mockOriginAccount = new AccountModel("123");
        mockOriginAccount.credit(new BigDecimal("50.00"));

        AccountModel mockDestinationAccount = new AccountModel("123");
        mockDestinationAccount.credit(new BigDecimal("50.00"));

        when(accountRepository.saveAll(any())).thenReturn(List.of(mockOriginAccount, mockDestinationAccount));

        Object result = accountService.transferEvent(event);

        assertNotNull(result);
        verify(accountRepository, times(2)).findById(anyString());
        verify(accountRepository, times(1)).saveAll(any());
    }

    @Test
    void testReset() {
        accountService.reset();

        verify(accountRepository, times(1)).deleteAll();
    }
}