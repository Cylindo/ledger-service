package com.wallet.ledger.util;

import com.example.common.exception.ValidationError;
import com.wallet.ledger.dto.AccountDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountValidator {
    public static List<ValidationError> validateAccount(AccountDTO accountDTO) {
        List<ValidationError> errors = new ArrayList<>();
        if (accountDTO == null) {
            errors.add(new ValidationError("accountDTO", "Account cannot be null",null));
            return errors;
        }

        if (accountDTO.getBalance() == null) {
            errors.add(new ValidationError("balance", "Account Balance cannot be null or empty",null));
        }

        if (accountDTO.getBalance() != null && accountDTO.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            errors.add(new ValidationError("balance", "Balance cannot be negative", null));
        }

        return errors;
    }
}
