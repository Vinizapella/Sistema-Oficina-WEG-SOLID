package com.weg.oficina.service;

import com.weg.oficina.dto.turma.TurmaRequestDto;
import com.weg.oficina.dto.turma.TurmaResponseDto;

public interface ITurmaService {

    TurmaResponseDto criar(
            TurmaRequestDto turmaRequestDto
    );

    void adicionarAluno(
            Long turmaId,
            Long alunoId
    );

}
