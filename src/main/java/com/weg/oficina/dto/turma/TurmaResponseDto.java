package com.weg.oficina.dto.turma;

import java.util.List;

public record TurmaResponseDto(

        Long id,

        String nome,

        List<String> nomesAlunos

) {
}
