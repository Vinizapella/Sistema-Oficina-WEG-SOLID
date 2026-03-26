package com.weg.oficina.mapper;

import com.weg.oficina.dto.ordemServico.OrdemServicoRequestDto;
import com.weg.oficina.dto.ordemServico.OrdemServicoResponseDto;
import com.weg.oficina.model.Aluno;
import com.weg.oficina.model.OrdemServico;
import com.weg.oficina.model.Professor;
import com.weg.oficina.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdemServicoMapper {

    public OrdemServico toEntity(
            OrdemServicoRequestDto ordemServicoRequestDto,
            Professor professor,
            List<Aluno> alunos
    ){
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setEquipamento(ordemServicoRequestDto.equipamento());
        ordemServico.setDefeitoRelatado(ordemServicoRequestDto.defeitoRelatado());
        ordemServico.setProfessor(professor);
        ordemServico.setExecutores(alunos);

        ordemServico.setStatus("EXECUTANDO");
        return ordemServico;
    }

    public OrdemServicoResponseDto toResponse(
            OrdemServico ordemServico
    ){
        return new OrdemServicoResponseDto(
                ordemServico.getId(),
                ordemServico.getEquipamento(),
                ordemServico.getDefeitoRelatado(),
                ordemServico.getStatus(),
                ordemServico.getMaterialUsados(),
                ordemServico.getLaudoTecnico(),
                ordemServico.getProfessor().getNome(),
                ordemServico.getExecutores()
                        .stream()
                        .map(Usuario::getNome)
                        .toList()
        );
    }
}
