package com.weg.oficina.service;

import com.weg.oficina.dto.ordemServico.OrdemServicoAtualizarDto;
import com.weg.oficina.dto.ordemServico.OrdemServicoRequestDto;
import com.weg.oficina.dto.ordemServico.OrdemServicoResponseDto;

public interface IOrdemServicoService {

    OrdemServicoResponseDto abrir(
            OrdemServicoRequestDto ordemServicoRequestDto
    );

    void atualizarStatus(
            Long id,
            OrdemServicoAtualizarDto ordemServicoAtualizarDto
    );

}
