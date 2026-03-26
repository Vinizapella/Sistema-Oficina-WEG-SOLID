package com.weg.oficina.dto.professor;

import com.weg.oficina.dto.UsuarioRequestDto;
import jakarta.validation.constraints.NotBlank;

public record ProfessorRequestDto(

        @NotBlank
        String nome,

        @NotBlank
        String especialidade

) implements UsuarioRequestDto {
}
