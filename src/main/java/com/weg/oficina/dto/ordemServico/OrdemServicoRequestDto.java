package com.weg.oficina.dto.ordemServico;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrdemServicoRequestDto (

        @NotBlank
        String equipamento,

        @NotBlank
        String defeitoRelatado,

        @NotNull
        Long professorId,

        @NotEmpty
        @Size(min = 1, max = 3)
        List<Long> alunosIds

){
}
