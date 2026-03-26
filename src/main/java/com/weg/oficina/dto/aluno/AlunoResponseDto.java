package com.weg.oficina.dto.aluno;

import com.weg.oficina.dto.UsuarioResponseDto;

public record AlunoResponseDto(

        Long id,

        String nome,

        String tipo,

        String matricula

) implements UsuarioResponseDto {
}
