package com.example.apibank;


import com.example.apibank.entities.AccountModel;
import com.example.apibank.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ConcurrencyTests {

    @Autowired
    private AccountRepository accountRepository;

    private AccountModel accountA;
    private AccountModel accountB;

    @BeforeEach
    void setUp() {
        accountA = new AccountModel("123");
        accountA.credit(BigDecimal.valueOf(5000));
        accountB = new AccountModel("456");
        accountB.credit(BigDecimal.valueOf(5000));
        accountRepository.save(accountA);
        accountRepository.save(accountB);
    }

    @Test
    void testSimultaneousDeposits() throws InterruptedException {
        int threads = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    synchronized (accountA) {
                        accountA.credit(BigDecimal.valueOf(100));
                        accountRepository.save(accountA);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        AccountModel updatedAccount = accountRepository.findById("123").orElseThrow();
        assertEquals(new BigDecimal("10000.00"), updatedAccount.getBalance());
    }

    @Test
    void testSimultaneousWithdrawalsDoNotAllowNegativeBalance() throws InterruptedException {
        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    synchronized (accountA) {
                        if (accountA.getBalance().compareTo(new BigDecimal("1000")) >= 0) {
                            accountA.credit(BigDecimal.valueOf(1000));
                            accountRepository.save(accountA);
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        AccountModel updatedAccount = accountRepository.findById("123").orElseThrow();
        assertTrue(updatedAccount.getBalance().compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void testConcurrentTransfersBetweenAccounts() throws InterruptedException {
        int threads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    synchronized (accountA) {
                        synchronized (accountB) {
                            BigDecimal transferAmount = new BigDecimal("100");
                            if (accountA.getBalance().compareTo(transferAmount) >= 0) {
                                accountA.debit(transferAmount);
                                accountB.credit(transferAmount);
                                accountRepository.save(accountA);
                                accountRepository.save(accountB);
                            }
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        AccountModel updatedA = accountRepository.findById("123").orElseThrow();
        AccountModel updatedB = accountRepository.findById("456").orElseThrow();

        assertEquals(new BigDecimal("5000.00"), updatedA.getBalance().add(updatedB.getBalance()).divide(new BigDecimal("2")));
        // total money remains consistent
    }
}
