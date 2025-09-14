package com.wallet.ledger.constants;

import lombok.Getter;

@Getter
public enum TransferType {
    CREDIT("CREDIT"),
    DEBIT("DEBIT");

    private final String type;

    TransferType(String type) {
        this.type = type;
    }
}
