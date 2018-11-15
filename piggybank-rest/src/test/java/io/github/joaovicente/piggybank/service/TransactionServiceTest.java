package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.dto.TransactionDto;
import io.github.joaovicente.piggybank.entity.Transaction;
import io.github.joaovicente.piggybank.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class TransactionServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private KidService kidService;

    private TransactionService transactionService;

    @Before
    public void before()    {
        transactionService = new TransactionService(transactionRepository, kidService);
    }

    @Test
    public void createTransaction() {
        final int RANDOMLY_GENERATED_UUID = 4;
        Transaction transaction = Transaction.builder()
                .build();
        TransactionDto requestDto = TransactionDto.builder()
                .build();
        // Given transactionRepository.insert() will return
        given(this.transactionRepository.insert(transaction)).willReturn(transaction);

        // when transactionService.createTransaction is called with a valid transaction
        IdResponseDto responseDto = transactionService.createTransaction(requestDto);

        // Then it will return the transactionId
        assertThat(responseDto).isInstanceOf(IdResponseDto.class);
        assertThat(UUID.fromString(responseDto.getId()).version()).isEqualTo(RANDOMLY_GENERATED_UUID);
    }

//    @Test
//    public void getKid() {
//        final String NAME = "Albert";
//        final Kid kid = Kid.builder()
//                .name(NAME)
//                .build();
//        final String id = kid.getId();
//        final KidReadDto kidReadDto = KidReadDto.builder()
//                .id(id)
//                .name(NAME)
//                .build();
//
//        given(this.kidRepository.findById(id))
//                .willReturn(Optional.of(kid));
//
//        // When
//        KidReadDto actual = kidService.getKidById(id);
//
//        // Then
//        assertThat(actual).isEqualTo(kidReadDto);
//    }
//
//    @Test(expected = KidNotFoundException.class)
//    public void getKidNotFound() {
//        String ID = "c9635e84-4111-4de9-b896-f506fc7bc25b";
//
//        Optional<Kid> empty = Optional.empty();
//        given(this.kidRepository.findById(ID))
//                .willReturn(empty);
//
//        kidService.getKidById(ID);
//    }
//
//
//    @Test
//    public void deleteKid() {
//        String ID = "c9635e84-4111-4de9-b896-f506fc7bc25b";
//
//        given(this.kidRepository.existsById(ID))
//                .willReturn(true);
//
//        kidService.deleteKidById(ID);
//
//        then(kidRepository)
//                .should()
//                .deleteById(ID);
//    }
//
//    @Test(expected = KidNotFoundException.class)
//    public void deleteKidNotFound() throws Exception {
//        String ID = "c9635e84-4111-4de9-b896-f506fc7bc25b";
//
//        given(this.kidRepository.existsById(ID))
//                .willReturn(false);
//        kidService.getKidById(ID);
//    }

}
