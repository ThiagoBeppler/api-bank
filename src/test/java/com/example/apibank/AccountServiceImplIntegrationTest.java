package com.example.apibank;

import com.example.apibank.dtos.EventDto;
import com.example.apibank.entities.AccountModel;
import com.example.apibank.repositories.AccountRepository;
import com.example.apibank.services.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(AccountServiceImpl.class)
class AccountServiceImplIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void testBalance_AccountExists() {
        AccountModel account = new AccountModel("123");
        account.credit(new BigDecimal ("100.00"));

        accountRepository.save(account);

        BigDecimal balance = accountService.balance("123");

        assertEquals(new BigDecimal("100.00"), balance);
    }

    @Test
    void testBalance_AccountNotFound() {
        BigDecimal balance = accountService.balance("123");
        assertNull(balance);
    }

    @Test
    void testTransferEvent_Deposit() {
        EventDto event = new EventDto("deposit", null, new BigDecimal("50.00"), "123");

        Object result = accountService.transferEvent(event);

        AccountModel account = accountRepository.findById("123").orElseThrow();
        assertEquals(new BigDecimal("50.00"), account.getBalance());
        assertNotNull(result);
    }

    @Test
    void testTransferEvent_Withdraw() {
        AccountModel account = new AccountModel("123");
        account.credit(new BigDecimal ("100.00"));

        accountRepository.save(account);

        EventDto event = new EventDto("withdraw", "123", new BigDecimal("50.00"), null);

        Object result = accountService.transferEvent(event);

        AccountModel updatedAccount = accountRepository.findById("123").orElseThrow();
        assertEquals(new BigDecimal("50.00"), updatedAccount.getBalance());
        assertNotNull(result);
    }

    @Test
    void testTransferEvent_Transfer() {
        AccountModel origin = new AccountModel("123");
        origin.credit(new BigDecimal ("100.00"));

        AccountModel destination = new AccountModel("456");
        destination.credit(new BigDecimal ("200.00"));

        accountRepository.save(origin);
        accountRepository.save(destination);

        EventDto event = new EventDto("transfer", "123", new BigDecimal("50.00"), "456");

        Object result = accountService.transferEvent(event);

        AccountModel updatedOrigin = accountRepository.findById("123").orElseThrow();
        AccountModel updatedDestination = accountRepository.findById("456").orElseThrow();

        assertEquals(new BigDecimal("50.00"), updatedOrigin.getBalance());
        assertEquals(new BigDecimal("250.00"), updatedDestination.getBalance());
        assertNotNull(result);
    }

    @Test
    void testReset() {
        accountRepository.save(new AccountModel("123"));
        accountRepository.save(new AccountModel("456"));

        accountService.reset();

        assertTrue(accountRepository.findAll().isEmpty());
    }
}