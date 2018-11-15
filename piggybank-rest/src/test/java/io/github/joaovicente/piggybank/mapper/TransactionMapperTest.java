package io.github.joaovicente.piggybank.mapper;

import io.github.joaovicente.piggybank.dto.TransactionDto;
import io.github.joaovicente.piggybank.entity.Transaction;
import io.github.joaovicente.piggybank.type.TransactionKind;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionMapperTest {
    private ModelMapper modelMapper = new ModelMapper();
    final int AMOUNT = 100;
    final Date DATE = new Date();
    final String DESCRIPTION = "Gift from grandparents";
    final String KID_ID = "51459636-51f8-4f90-ac17-2439b5a4de59";
    final int RANDOMLY_GENERATED_UUID = 4;

    @Test
    public void TransactionToTransactionDto() {
        Transaction transaction = Transaction.builder()
                .amount(AMOUNT)
                .date(new Date())
                .description(DESCRIPTION)
                .kidId(KID_ID)
                .kind(TransactionKind.CREDIT)
                .build();

        TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);
        assertThat(transactionDto.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionDto.getDate()).isEqualTo(transaction.getDate());
        assertThat(transactionDto.getDescription()).isEqualTo(transaction.getDescription());
        assertThat(transactionDto.getKidId()).isEqualTo(transaction.getKidId());
        assertThat(transactionDto.getKind()).isEqualTo(transaction.getKind());
        assertThat(transactionDto.getId()).isEqualTo(transaction.getId());
        assertThat(UUID.fromString(transactionDto.getId()).version()).isEqualTo(RANDOMLY_GENERATED_UUID);
    }

    @Test
    public void TransactionDtoToTransaction() {
        TransactionDto transactionDto = TransactionDto.builder()
                .amount(AMOUNT)
                .date(new Date())
                .description(DESCRIPTION)
                .kidId(KID_ID)
                .kind(TransactionKind.CREDIT)
                .build();
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        assertThat(transaction.getAmount()).isEqualTo(transactionDto.getAmount());
        assertThat(transaction.getDate()).isEqualTo(transactionDto.getDate());
        assertThat(transaction.getDescription()).isEqualTo(transactionDto.getDescription());
        assertThat(transaction.getKidId()).isEqualTo(transactionDto.getKidId());
        assertThat(transaction.getKind()).isEqualTo(transactionDto.getKind());
        // transactionId should always be generated at the entity, even if provided in the DTO
        assertThat(UUID.fromString(transaction.getId()).version()).isEqualTo(RANDOMLY_GENERATED_UUID);
    }
}
