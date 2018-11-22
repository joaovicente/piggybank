package io.github.joaovicente.piggybank.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

import io.github.joaovicente.piggybank.entity.Kid;

import io.github.joaovicente.piggybank.repository.KidRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        // Given
        given(this.kidRepository.save(kid)).willReturn(kid);

        // When
        Kid kidInserted = kidService.createKid(kid);

        // Then
        assertThat(kidInserted).isEqualTo(kid);
    }


    @Test
    public void getKids() {
        List<Kid> kidsList = new ArrayList<>();
        final String KID1 = "Albert";
        final String KID2 = "Beth";
        kidsList.add(new Kid(KID1));
        kidsList.add(new Kid(KID2));

        given(this.kidRepository.findAll())
                .willReturn(kidsList);

        // When
        List<Kid> actual = kidService.getKids();

        // Then
        assertThat(actual.get(0).getName()).isEqualTo(KID1);
        assertThat(actual.get(1).getName()).isEqualTo(KID2);
    }


    @Test
    public void getKid() {
        final String NAME = "Albert";
        final Kid kid = Kid.builder()
                .name(NAME)
                .build();
        final String id = kid.getId();

        given(this.kidRepository.findById(id))
                .willReturn(java.util.Optional.of(kid));

        // When
        Kid actual = kidService.getKidById(id);

        // Then
        assertThat(actual).isEqualTo(kid);
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
    public void deleteKidNotFound() {
        String ID = "c9635e84-4111-4de9-b896-f506fc7bc25b";

        given(this.kidRepository.existsById(ID))
                .willReturn(false);
        kidService.getKidById(ID);
    }

}
