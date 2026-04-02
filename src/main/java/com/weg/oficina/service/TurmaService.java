package com.weg.oficina.service;

import com.weg.oficina.dto.turma.TurmaRequestDto;
import com.weg.oficina.dto.turma.TurmaResponseDto;
import com.weg.oficina.mapper.TurmaMapper;
import com.weg.oficina.model.Aluno;
import com.weg.oficina.model.Professor;
import com.weg.oficina.repository.TurmaRepository;
import com.weg.oficina.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TurmaService implements ITurmaService{

    private final TurmaRepository turmaRepository;

    private final TurmaMapper turmaMapper;

    private final UsuarioRepository usuarioRepository;

    public TurmaService(
            TurmaRepository turmaRepository,
            TurmaMapper turmaMapper,
            UsuarioRepository usuarioRepository
    ) {
        this.turmaRepository = turmaRepository;
        this.turmaMapper = turmaMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public TurmaResponseDto criar(
            TurmaRequestDto turmaResponseDto
    ) {
        return turmaMapper.toResponse(turmaRepository.save(turmaMapper.toEntity(turmaResponseDto)));
    }

    @Override
    @Transactional
    public void adicionarAluno(
            Long turmaId,
            Long alunoId
    ) {
        var turma = turmaRepository.findById(turmaId)
                .orElseThrow();

        var usuario = usuarioRepository.findById(alunoId)
                .orElseThrow(()->new RuntimeException("Aluno não encontrado"));

        if (!(usuario instanceof Aluno aluno)){
            throw new RuntimeException("Apenas alunos podem ser adicionados a uma turma");
        }

        turma.getAlunos().add(aluno);
    }
}
