package com.weg.oficina.dto.ordemServico;

import jakarta.validation.constraints.NotBlank;

public record OrdemServicoAtualizarDto(

        @NotBlank
        String novoStatus,

        @NotBlank
        String laudo,

        @NotBlank
        String materiais
) {
}
