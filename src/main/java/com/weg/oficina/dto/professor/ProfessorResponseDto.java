package com.weg.oficina.dto.professor;

import com.weg.oficina.dto.UsuarioResponseDto;

public record ProfessorResponseDto(

        Long id,

        String nome,

        String tipo,

        String especialidade

) implements UsuarioResponseDto {
}
