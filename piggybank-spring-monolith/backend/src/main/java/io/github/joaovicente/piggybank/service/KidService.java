package io.github.joaovicente.piggybank.service;

import io.github.joaovicente.piggybank.repository.KidRepository;
import io.github.joaovicente.piggybank.entity.Kid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KidService {
    private final KidRepository kidRepository;

    @Autowired
    KidService(KidRepository kidRepository) {
        this.kidRepository = kidRepository;
    }

    public Kid createKid(Kid kid) {
        return kidRepository.save(kid);
    }

    public boolean kidExists(String kidId)  {
        return kidRepository.existsById(kidId);
    }

    public Kid getKidById(String id)  {
        Optional<Kid> optionalKid = kidRepository.findById(id);
        Kid kid;
        if (optionalKid.isPresent())  {
            kid = optionalKid.get();
        }
        else {
            throw new KidNotFoundException();
        }
        return kid;
    }

    public List<Kid> getKids()  {
        List<Kid> kidList = new ArrayList<>();
        kidRepository.findAll().forEach(kidList::add);
        return kidList;
    }

    public void deleteKidById(String id)  {
        if (kidRepository.existsById(id)) {
            kidRepository.deleteById(id);
        }
        else    {
            throw new KidNotFoundException();
        }
    }
}
