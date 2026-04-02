package com.weg.oficina.mapper;

import com.weg.oficina.dto.UsuarioRequestDto;
import com.weg.oficina.dto.UsuarioResponseDto;
import com.weg.oficina.dto.aluno.AlunoRequestDto;
import com.weg.oficina.dto.aluno.AlunoResponseDto;
import com.weg.oficina.dto.professor.ProfessorRequestDto;
import com.weg.oficina.dto.professor.ProfessorResponseDto;
import com.weg.oficina.model.Aluno;
import com.weg.oficina.model.Professor;
import com.weg.oficina.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(
            UsuarioRequestDto usuarioRequestDto
    ){
        if (usuarioRequestDto instanceof AlunoRequestDto alunoRequestDto){
            Aluno aluno = new Aluno();
            aluno.setNome(alunoRequestDto.nome());
            aluno.setTipo("ALUNO");
            aluno.setMatricula(alunoRequestDto.matricula());
            return aluno;
        }else if (usuarioRequestDto instanceof ProfessorRequestDto professorRequestDto){
            Professor professor = new Professor();
            professor.setNome(professorRequestDto.nome());
            professor.setTipo("PROFESSOR");
            professor.setEspecialidade(professorRequestDto.especialidade());
            return professor;
        }
        throw  new RuntimeException("Tipo de usuario descohecido");
    }

    public UsuarioResponseDto toResponse(
            Usuario usuario
    ){
        if (usuario instanceof Aluno aluno){
            return new AlunoResponseDto(
                    aluno.getId(),
                    aluno.getNome(),
                    aluno.getTipo(),
                    aluno.getMatricula()
                    );
        }else if (usuario instanceof Professor professor){
            return new ProfessorResponseDto(
                    professor.getId(),
                    professor.getNome(),
                    professor.getEspecialidade(),
                    professor.getTipo()
            );
        }
        throw  new RuntimeException("Tipo de usuario descohecido");
    }

}
