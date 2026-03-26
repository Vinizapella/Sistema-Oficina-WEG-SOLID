package com.weg.oficina.service;

import com.weg.oficina.dto.ordemServico.OrdemServicoRequestDto;
import com.weg.oficina.dto.ordemServico.OrdemServicoResponseDto;

public interface IOrdemServicoService {

    OrdemServicoResponseDto abrir(
            OrdemServicoRequestDto ordemServicoRequestDto
    );

    void atualizarStatus(
            Long id,
            String novoStatus,
            String laudo,
            String materiais
    );

}
