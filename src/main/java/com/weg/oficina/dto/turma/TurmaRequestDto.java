package com.weg.oficina.dto.turma;

import jakarta.validation.constraints.NotBlank;

public record TurmaRequestDto(

        @NotBlank
        String nome

) {
}
