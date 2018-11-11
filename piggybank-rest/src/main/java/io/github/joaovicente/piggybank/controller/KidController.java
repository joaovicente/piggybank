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

@RestController
public class KidController {
    private final KidService kidService;

    @Autowired
    KidController(KidService kidService)   {
        this.kidService = kidService;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            // TODO: Validate payload and return 400 if not ok
    })
    @RequestMapping(value = "/kids", method = RequestMethod.POST)
    public IdResponseDto createKid(@RequestBody KidCreateDto reqBody)    {
        return kidService.createKid(reqBody);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(value = "/kids/{id}", method = RequestMethod.GET)
    public KidReadDto getKid(@PathVariable(name="id") String id )    {
        KidReadDto dto;
        try {
            dto = kidService.getKidById(id);
        }
        catch(EntityNotFoundException e)  {
            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message("Supplied kid id was not found")
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
        return dto;
    }


    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @RequestMapping(value = "/kids/{id}", method = RequestMethod.DELETE)
    public void deleteKid(@PathVariable(name="id") String id )    {
        try {
            kidService.deleteKidById(id);
        }
        catch(EntityNotFoundException e)  {
            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message("Supplied kid id was not found")
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
    }
}
