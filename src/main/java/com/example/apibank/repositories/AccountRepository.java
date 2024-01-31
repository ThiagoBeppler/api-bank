package com.example.apibank.repositories;

import com.example.apibank.entities.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountModel, String> {
}
