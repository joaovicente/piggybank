package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.dto.KidCreateDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.dto.KidReadDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

import io.github.joaovicente.piggybank.model.Kid;

import io.github.joaovicente.piggybank.repository.KidRepository;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
public class KidServiceTest {

    @MockBean
    private KidRepository kidRepository;

    private KidService kidService;


    @Before
    public void before()    {
        kidService = new KidService(kidRepository);
    }


    @Test
    public void createKid() {
        final String NAME = "Albert";
        final Kid kid = Kid.builder()
                .name(NAME)
                .build();
        final int RANDOMLY_GENERATED_UUID = 4;

        KidCreateDto requestDto = KidCreateDto.builder()
                .name(NAME)
                .build();
        IdResponseDto responseDto = IdResponseDto.builder()
                .id(kid.getId())
                .build();

        given(this.kidRepository.insert(kid)).willReturn(kid);

        // When
        IdResponseDto actual = kidService.createKid(requestDto);

        // Then
        assertThat(actual).isInstanceOf(IdResponseDto.class);
        assertThat(UUID.fromString(actual.getId()).version()).isEqualTo(RANDOMLY_GENERATED_UUID);
    }

    @Test
    public void getKid() {
        final String NAME = "Albert";
        final Kid kid = Kid.builder()
                .name(NAME)
                .build();
        final String id = kid.getId();
        final KidReadDto kidReadDto = KidReadDto.builder()
                .id(id)
                .name(NAME)
                .build();

        given(this.kidRepository.findById(id))
                .willReturn(java.util.Optional.of(kid));

        // When
        KidReadDto actual = kidService.getKidById(id);

        // Then
        assertThat(actual).isEqualTo(kidReadDto);
    }

    @Test(expected = KidNotFoundException.class)
    public void getKidNotFound() {
        String ID = "c9635e84-4111-4de9-b896-f506fc7bc25b";

        Optional<Kid> empty = Optional.empty();
        given(this.kidRepository.findById(ID))
                .willReturn(empty);

        kidService.getKidById(ID);
    }


    @Test
    public void deleteKid() {
        String ID = "c9635e84-4111-4de9-b896-f506fc7bc25b";

        given(this.kidRepository.existsById(ID))
                .willReturn(true);

        kidService.deleteKidById(ID);

        then(kidRepository)
                .should()
                .deleteById(ID);
    }

    @Test(expected = KidNotFoundException.class)
    public void deleteKidNotFound() throws Exception {
        String ID = "c9635e84-4111-4de9-b896-f506fc7bc25b";

        given(this.kidRepository.existsById(ID))
                .willReturn(false);
        kidService.getKidById(ID);
    }

}
