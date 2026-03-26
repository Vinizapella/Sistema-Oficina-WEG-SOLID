package com.weg.oficina.mapper;

import com.weg.oficina.dto.turma.TurmaRequestDto;
import com.weg.oficina.dto.turma.TurmaResponseDto;
import com.weg.oficina.model.Turma;
import com.weg.oficina.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TurmaMapper {

    public Turma toEntity(
            TurmaRequestDto turmaRequestDto
    ){
        Turma turma = new Turma();
        turma.setNome(turmaRequestDto.nome());
        turma.setAlunos(new ArrayList<>());
        return turma;
    }

    public TurmaResponseDto toResponse(
            Turma turma
    ){
        List<String> nomesAlunos = new ArrayList<>();

        if (turma.getAlunos() != null){
            nomesAlunos = turma.getAlunos()
                    .stream()
                    .map(Usuario::getNome)
                    .toList();
        }

        return new TurmaResponseDto(
                turma.getId(),
                turma.getNome(),
                nomesAlunos
        );
    }

}
