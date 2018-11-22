package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.dto.KidAndBalancesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryService {
    private final KidService kidService;
    private final BalanceService balanceService;

    @Autowired
    public QueryService(KidService kidService, BalanceService balanceService) {
        this.kidService = kidService;
        this.balanceService = balanceService;
    }

    public List<KidAndBalancesDto> getKidsAndBalance()  {
        return kidService.getKids().stream()
                .map(kid -> KidAndBalancesDto.builder()
                       .kidId(kid.getId())
                       .kidName(kid.getName())
                       .kidBalance(balanceService.calculateKidBalance(kid.getId()))
                       .build())
                .collect(Collectors.toList());
    }
}
