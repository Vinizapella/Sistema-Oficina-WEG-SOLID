package com.weg.oficina.dto.aluno;

import com.weg.oficina.dto.UsuarioRequestDto;
import jakarta.validation.constraints.NotBlank;

public record AlunoRequestDto (

        @NotBlank
        String nome,

        @NotBlank
        String matricula

) implements UsuarioRequestDto {
}
