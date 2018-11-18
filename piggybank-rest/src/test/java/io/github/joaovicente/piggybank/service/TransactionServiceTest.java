package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.entity.Transaction;
import io.github.joaovicente.piggybank.repository.TransactionRepository;
import io.github.joaovicente.piggybank.type.TransactionKind;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.*;

import static org.apache.commons.lang3.time.DateUtils.parseDateStrictly;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
public class TransactionServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private KidService kidService;

    private TransactionService transactionService;
    private final String KID_ID1  = "k1";
    private final String KID_ID2  = "k2";
    private final String TRANSACTION_ID1  = "t1";
    private final int AMOUNT_100 = 100;
    private final String AMOUNT_100_STR = Integer.toString(AMOUNT_100);
    private final int AMOUNT_200 = 200;
    private final String AMOUNT_200_STR = Integer.toString(AMOUNT_200);
    private final String AMOUNT_0 = "0";
    final String DATE_PATTERN = "yyyy-MM-dd";
    private final String DATE1_STR = "2017-01-01";
    private Date DATE1;
    private final String DATE2_STR = "2018-02-02";
    private Date DATE2;
    private final String KIND_BAD = "BAD_KIND";
    private final String DESCRIPTION_CREDIT1 = "Gift from grandparents";
    private final String DESCRIPTION_DEBIT1 = "Shopping day";

    public TransactionServiceTest() {
        try {
            DATE1 = parseDateStrictly(DATE1_STR, DATE_PATTERN);
            DATE2 = parseDateStrictly(DATE2_STR, DATE_PATTERN);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void before()    {
        transactionService = new TransactionService(transactionRepository, kidService);
    }

    @Test
    public void createTransaction() {
        Transaction transaction = Transaction.builder()
                .date(DATE1)
                .kind(TransactionKind.CREDIT)
                .amount(AMOUNT_100)
                .description(DESCRIPTION_CREDIT1)
                .build();
        // Given transactionRepository.insert() will return
        given(this.transactionRepository.insert(transaction)).willReturn(transaction);

        // when transactionService.createTransaction is called with a valid transaction
        Transaction insertedTransaction = transactionService.createTransaction(transaction);

        // Then it will return the transactionId
        assertThat(insertedTransaction).isEqualTo(transaction);
    }

    @Test
    public void getTransactionById() {
        final Transaction transaction = Transaction.builder()
                .date(DATE1)
                .kind(TransactionKind.CREDIT)
                .amount(AMOUNT_100)
                .description(DESCRIPTION_CREDIT1)
                .build();
        final String id = transaction.getId();

        given(this.transactionRepository.findById(id))
                .willReturn(Optional.of(transaction));

        // When
        Transaction actual = transactionService.getTransactionById(id);

        // Then
        assertThat(actual).isEqualTo(transaction);
    }

    @Test(expected = TransactionNotFoundException.class)
    public void getTransactionByIdNotFound() {
        String ID = "c9635e84-4111-4de9-b896-f506fc7bc25b";

        Optional<Transaction> empty = Optional.empty();
        given(this.transactionRepository.findById(ID))
                .willReturn(empty);

        transactionService.getTransactionById(ID);
    }

    @Test
    public void getTransactionsByKidId() {
        List<Transaction> transactionListFromRepo = new ArrayList<>();
        transactionListFromRepo.add(
            Transaction.builder()
                    .date(DATE1)
                    .kidId(KID_ID1)
                    .kind(TransactionKind.CREDIT)
                    .amount(AMOUNT_100)
                    .description(DESCRIPTION_CREDIT1)
                    .build());
        transactionListFromRepo.add(
            Transaction.builder()
                    .date(DATE2)
                    .kidId(KID_ID1)
                    .kind(TransactionKind.DEBIT)
                    .amount(AMOUNT_100)
                    .description(DESCRIPTION_DEBIT1)
                    .build());

        given(this.kidService.kidExists(KID_ID1))
                .willReturn(true);
        given(this.transactionRepository.findByKidId(KID_ID1))
                .willReturn(transactionListFromRepo);

        // When
        List<Transaction> transactionListFromService = transactionService.getTransactionsByKidId(KID_ID1);

        // Then
        assertThat(transactionListFromService).isEqualTo(transactionListFromRepo);
    }

    @Test(expected = TransactionNotFoundException.class)
    public void getTransactionsByKidIdNotFound() {
        Optional<Transaction> empty = Optional.empty();
        given(this.kidService.kidExists(KID_ID1))
                .willReturn(false);
        transactionService.getTransactionById(KID_ID1);
    }

    @Test
    public void deleteTransaction() {
        final Transaction transaction = Transaction.builder()
                .date(DATE1)
                .kind(TransactionKind.CREDIT)
                .amount(AMOUNT_100)
                .description(DESCRIPTION_CREDIT1)
                .build();

        given(this.transactionRepository.findById(transaction.getId()))
                .willReturn(Optional.of(transaction));

        transactionService.deleteTransaction(transaction.getId());

        then(transactionRepository)
                .should()
                .deleteById(transaction.getId());
    }

    @Test(expected = TransactionNotFoundException.class)
    public void deleteTransactionNotFound() throws Exception {
        String ID = "c9635e84-4111-4de9-b896-f506fc7bc25b";

        given(this.transactionRepository.existsById(ID))
                .willReturn(false);
        transactionService.getTransactionById(ID);
    }

}
