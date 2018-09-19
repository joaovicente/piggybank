package io.github.joaovicente.piggybank;

import io.github.joaovicente.piggybank.model.Transaction;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionTest {
    private final String SOME_DESCRIPTION = "first transaction";

    @Test
    public void transactionTest() {
        Transaction transaction = Transaction.builder()
                .description(SOME_DESCRIPTION)
                .amount(10)
                .kind(Transaction.Kind.CREDIT)
                .build();

        assertSame(SOME_DESCRIPTION, transaction.getDescription());
        assertSame(10, transaction.getAmount());
        assertSame(Transaction.Kind.CREDIT, transaction.getKind());
    }
}