package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.dao.KidRepository;
import io.github.joaovicente.piggybank.dto.CreateKidRequestDto;
import io.github.joaovicente.piggybank.dto.GetKidResponseDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.model.Kid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KidService {
    private final KidRepository kidRepository;

    @Autowired
    KidService(KidRepository kidRepository) {
        this.kidRepository = kidRepository;
    }

    public IdResponseDto createKid(CreateKidRequestDto req) {
        Kid kid = Kid.builder()
                .name(req.getName())
                .build();
        kidRepository.insert(kid);
        return new IdResponseDto(kid.getId());
    }

    public GetKidResponseDto getKidById(String id)  {
        Optional<Kid> optionalKid = kidRepository.findById(id);
        GetKidResponseDto dto;
        if (optionalKid.isPresent())  {
            Kid kid = optionalKid.get();
            dto = GetKidResponseDto.builder()
                    .id(kid.getId())
                    .name(kid.getName())
                    .build();
        }
        else {
            throw new EntityNotFoundException();
        }
        return dto;
    }

    public void deleteKidById(String id)  {
        if (kidRepository.existsById(id)) {
            kidRepository.deleteById(id);
        }
        else    {
            throw new EntityNotFoundException();
        }
    }
}
