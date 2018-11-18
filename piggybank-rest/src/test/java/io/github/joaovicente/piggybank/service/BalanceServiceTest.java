package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.entity.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static io.github.joaovicente.piggybank.type.TransactionKind.CREDIT;
import static io.github.joaovicente.piggybank.type.TransactionKind.DEBIT;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
public class BalanceServiceTest {

    @MockBean
    private KidService kidService;
    @MockBean
    private TransactionService transactionService;

    private BalanceService balanceService;

    @Before
    public void before()    {
        balanceService = new BalanceService(kidService, transactionService);
    }

    @Test
    public void balance() {
        final String KID1 = "k1";
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(Transaction.builder().kind(CREDIT).amount(100).build());
        transactionList.add(Transaction.builder().kind(DEBIT).amount(50).build());
        transactionList.add(Transaction.builder().kind(CREDIT).amount(30).build());

        // Given
        given(kidService.kidExists(KID1)).willReturn(true);
        given(transactionService.getTransactionsByKidId(KID1)).willReturn(transactionList);

        // When
        int balance = balanceService.calculateKidBalance(KID1);

        //Then
        assertThat(balance).isEqualTo(80);

    }
}
