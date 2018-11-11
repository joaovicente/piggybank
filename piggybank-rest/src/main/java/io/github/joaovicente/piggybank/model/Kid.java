package io.github.joaovicente.piggybank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Kid {
    @Id
    private final String id = UUID.randomUUID().toString();
    private String name;
}
