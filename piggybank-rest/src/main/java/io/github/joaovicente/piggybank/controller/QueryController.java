package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.KidAndBalancesDto;
import io.github.joaovicente.piggybank.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryController {
    QueryService queryService;

    @Autowired
    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping(path = "/kids-and-balances")
    public List<KidAndBalancesDto> getKidsAndBalance()  {
        return queryService.getKidsAndBalance();
    }
}
