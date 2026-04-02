package com.weg.oficina.dto.ordemServico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrdemServicoAtualizarDto(

        @NotBlank
        String novoStatus,

        @NotBlank
        String laudo,

        @NotBlank
        String materiais,

        @NotNull
        Long professorId

) {
}
