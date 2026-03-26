package com.weg.oficina.dto.ordemServico;

import java.util.List;

public record OrdemServicoResponseDto(

        Long id,

        String equipamento,

        String defeitoRelatado,

        String status,

        String materiaisUsados,

        String laudoTecnico,

        String nomeProfessor,

        List<String> nomesAlunos

) {
}
