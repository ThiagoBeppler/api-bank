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
        AccountModel account = new AccountModel("123", 100.0f);
        accountRepository.save(account);

        Float balance = accountService.balance("123");

        assertEquals(100.0f, balance);
    }

    @Test
    void testBalance_AccountNotFound() {
        Exception exception = assertThrows(RuntimeException.class, () -> accountService.balance("123"));
        assertTrue(exception.getMessage().contains("Account not found!"));
    }

    @Test
    void testTransferEvent_Deposit() {
        EventDto event = new EventDto("deposit", null, 50.0f, "123");

        Object result = accountService.transferEvent(event);

        AccountModel account = accountRepository.findById("123").orElseThrow();
        assertEquals(50.0f, account.getBalance());
        assertNotNull(result);
    }

    @Test
    void testTransferEvent_Withdraw() {
        AccountModel account = new AccountModel("123", 100.0f);
        accountRepository.save(account);

        EventDto event = new EventDto("withdraw", "123", 50.0f, null);

        Object result = accountService.transferEvent(event);

        AccountModel updatedAccount = accountRepository.findById("123").orElseThrow();
        assertEquals(50.0f, updatedAccount.getBalance());
        assertNotNull(result);
    }

    @Test
    void testTransferEvent_Transfer() {
        AccountModel origin = new AccountModel("123", 100.0f);
        AccountModel destination = new AccountModel("456", 200.0f);
        accountRepository.save(origin);
        accountRepository.save(destination);

        EventDto event = new EventDto("transfer", "123", 50.0f, "456");

        Object result = accountService.transferEvent(event);

        AccountModel updatedOrigin = accountRepository.findById("123").orElseThrow();
        AccountModel updatedDestination = accountRepository.findById("456").orElseThrow();

        assertEquals(50.0f, updatedOrigin.getBalance());
        assertEquals(250.0f, updatedDestination.getBalance());
        assertNotNull(result);
    }

    @Test
    void testReset() {
        accountRepository.save(new AccountModel("123", 100.0f));
        accountRepository.save(new AccountModel("456", 200.0f));

        accountService.reset();

        assertTrue(accountRepository.findAll().isEmpty());
    }
}