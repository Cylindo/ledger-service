package com.wallet.ledger.service;

import com.wallet.ledger.entity.Account;
import com.wallet.ledger.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AccountServiceProcessor {

    private final AccountRepository accountRepository;

    public AccountServiceProcessor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account findById(Long id) {
//        Optional<Account> optionalAccount = accountRepository.findById(id);
//        if (optionalAccount.isPresent()) {
//            return optionalAccount.get();
//        } else {
//            log.error("Account with ID {} not found", id);
//            throw new RuntimeException("Account not found with ID: " + id);
//        }
        return accountRepository.findById(id).orElse(null);
    }
}
