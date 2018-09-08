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

        assertSame(transaction.getDescription(), SOME_DESCRIPTION);
        assertSame(transaction.getAmount(), 10);
        assertSame(transaction.getKind(), Transaction.Kind.CREDIT);
//        System.out.println(transaction.toString());
    }

}