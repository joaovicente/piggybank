package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.KidCreateDto;
import io.github.joaovicente.piggybank.dto.ErrorDto;
import io.github.joaovicente.piggybank.dto.KidReadDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.service.EntityNotFoundException;
import io.github.joaovicente.piggybank.service.KidService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
public class KidController {
    private final KidService kidService;

    @Autowired
    KidController(KidService kidService)   {
        this.kidService = kidService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @PostMapping(value = "/kids")
    public IdResponseDto createKid(@Valid @RequestBody KidCreateDto reqBody)    {
        return kidService.createKid(reqBody);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @GetMapping(value = "/kids/{id}")
    public KidReadDto getKid(@PathVariable(name="id") String id )    {
        KidReadDto dto;
        try {
            dto = kidService.getKidById(id);
        }
        catch(EntityNotFoundException e)  {

            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message(Collections.singletonList("Supplied kid id was not found"))
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
        return dto;
    }


    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @DeleteMapping(value = "/kids/{id}")
    public void deleteKid(@PathVariable(name="id") String id )    {
        try {
            kidService.deleteKidById(id);
        }
        catch(EntityNotFoundException e)  {
            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message(Collections.singletonList("Supplied kid id was not found"))
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
    }
}
