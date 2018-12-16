package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.dto.KidsAndBalancesDto;
import io.github.joaovicente.piggybank.entity.Kid;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class QueryServiceTest {
    @MockBean
    private KidService kidService;
    @MockBean
    private BalanceService balanceService;

    private QueryService queryService;

    @Before
    public void before() {
        queryService = new QueryService(kidService, balanceService);
    }

    @Test
    public void kidsAndBalance()    {
        final String KID1_NAME = "Albert";
        final String KID2_NAME = "Beth";
        final String KID3_NAME = "Carol";
        final int KID1_BALANCE= 10;
        final int KID2_BALANCE= 20;
        final int KID3_BALANCE= 30;

        List<Kid> kidList= new ArrayList<>();
        kidList.add(Kid.builder().name(KID1_NAME).build());
        kidList.add(Kid.builder().name(KID2_NAME).build());
        kidList.add(Kid.builder().name(KID3_NAME).build());

        // Given
        given(kidService.getKids()).willReturn(kidList);
        given(balanceService.calculateKidBalance(kidList.get(0).getId())).willReturn(KID1_BALANCE);
        given(balanceService.calculateKidBalance(kidList.get(1).getId())).willReturn(KID2_BALANCE);
        given(balanceService.calculateKidBalance(kidList.get(2).getId())).willReturn(KID3_BALANCE);

        // When
        List<KidsAndBalancesDto> response = queryService.getKidsAndBalance();

        // Then
        assertThat(response.get(0).getKidId()).isEqualTo(kidList.get(0).getId());
        assertThat(response.get(0).getKidName()).isEqualTo(KID1_NAME);
        assertThat(response.get(0).getKidBalance()).isEqualTo(KID1_BALANCE);
        assertThat(response.get(1).getKidId()).isEqualTo(kidList.get(1).getId());
        assertThat(response.get(1).getKidName()).isEqualTo(KID2_NAME);
        assertThat(response.get(1).getKidBalance()).isEqualTo(KID2_BALANCE);
        assertThat(response.get(2).getKidId()).isEqualTo(kidList.get(2).getId());
        assertThat(response.get(2).getKidName()).isEqualTo(KID3_NAME);
        assertThat(response.get(2).getKidBalance()).isEqualTo(KID3_BALANCE);
    }
}
