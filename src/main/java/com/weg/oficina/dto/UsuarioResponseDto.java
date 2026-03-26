package com.weg.oficina.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.weg.oficina.dto.aluno.AlunoRequestDto;
import com.weg.oficina.dto.aluno.AlunoResponseDto;
import com.weg.oficina.dto.professor.ProfessorResponseDto;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlunoResponseDto.class, name = "ALUNO"),
        @JsonSubTypes.Type(value = ProfessorResponseDto.class, name = "PROFESSOR")
})
public interface UsuarioResponseDto {

    Long id();

    String nome();

    String tipo();

}
