package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.KidDto;
import io.github.joaovicente.piggybank.dto.ErrorDto;
import io.github.joaovicente.piggybank.dto.KidReadDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.entity.Kid;
import io.github.joaovicente.piggybank.service.KidNotFoundException;
import io.github.joaovicente.piggybank.service.KidService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
public class KidController {
    private final KidService kidService;
    private final ModelMapper modelMapper;

    @Autowired
    KidController(KidService kidService, ModelMapper modelMapper)   {
        this.kidService = kidService;
        this.modelMapper = modelMapper;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @PostMapping(value = "/kids")
    public KidDto createKid(@Valid @RequestBody KidDto request)    {
        Kid kid = kidService.createKid(modelMapper.map(request, Kid.class));
        return modelMapper.map(kid, KidDto.class);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @GetMapping(value = "/kids/{id}")
    public KidDto getKid(@PathVariable(name="id") String id )    {
        Kid kid;
        try {
            kid = kidService.getKidById(id);
        }
        catch(KidNotFoundException e)  {

            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message(Collections.singletonList("kidId not found"))
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(kid, KidDto.class);
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
        catch(KidNotFoundException e)  {
            ErrorDto errorDto = ErrorDto.builder()
                    .error("NOT_FOUND")
                    .message(Collections.singletonList("Supplied kid id was not found"))
                    .build();
            throw new RestResponseException(errorDto, HttpStatus.NOT_FOUND);
        }
    }
}
