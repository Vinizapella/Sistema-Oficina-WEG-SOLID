package com.weg.oficina.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.weg.oficina.dto.aluno.AlunoRequestDto;
import com.weg.oficina.dto.professor.ProfessorRequestDto;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo_usuario")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlunoRequestDto.class, name = "ALUNO"),
        @JsonSubTypes.Type(value = ProfessorRequestDto.class, name = "PROFESSOR")
})
public interface UsuarioRequestDto{

    String nome();

}
