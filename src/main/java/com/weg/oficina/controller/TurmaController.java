package com.weg.oficina.controller;


import com.weg.oficina.dto.turma.TurmaRequestDto;
import com.weg.oficina.dto.turma.TurmaResponseDto;
import com.weg.oficina.service.ITurmaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    private final ITurmaService turmaService;

    public TurmaController(
            ITurmaService turmaService
    ) {
        this.turmaService = turmaService;
    }

    @PostMapping
    public TurmaResponseDto criar(
            @RequestBody @Valid TurmaRequestDto turmaRequestDto
    ){
        return turmaService.criar(turmaRequestDto);
    }

    @PutMapping("/{turmaId}/aluno/{alunoId}")
    void adicionarAluno(
           @PathVariable Long turmaId,
           @PathVariable Long alunoId
    ){
        turmaService.adicionarAluno(turmaId, alunoId);
    }
}
