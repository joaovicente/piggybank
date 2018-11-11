package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.dto.KidCreateDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.joaovicente.piggybank.model.Kid;

import io.github.joaovicente.piggybank.dao.KidRepository;

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
    public void createKid() throws Exception {
        final String ID = "1";
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
    public void getKid() throws Exception {
    }

    @Test
    public void getKidNotFound() throws Exception {
    }


    @Test
    public void deleteKid() throws Exception {
    }

    @Test
    public void deleteKidNotFound() throws Exception {
    }

}
