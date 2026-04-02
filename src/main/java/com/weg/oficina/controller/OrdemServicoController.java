package com.weg.oficina.controller;

import com.weg.oficina.dto.ordemServico.OrdemServicoAtualizarDto;
import com.weg.oficina.dto.ordemServico.OrdemServicoRequestDto;
import com.weg.oficina.dto.ordemServico.OrdemServicoResponseDto;
import com.weg.oficina.service.IOrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordens")
public class OrdemServicoController {

    private final IOrdemServicoService iOrdemServicoService;

    public OrdemServicoController(
            IOrdemServicoService iOrdemServicoService
    ) {
        this.iOrdemServicoService = iOrdemServicoService;
    }

    @PostMapping
    public OrdemServicoResponseDto criar(
            @Valid @RequestBody OrdemServicoRequestDto ordemServicoRequestDto
    ){
        return iOrdemServicoService.abrir(ordemServicoRequestDto);
    }

    @PutMapping("/{id}")
    void atualiza(
            @PathVariable Long id,
            @Valid @RequestBody OrdemServicoAtualizarDto ordemServicoAtualizarDto
    ){
        iOrdemServicoService.atualizarStatus(id, ordemServicoAtualizarDto);
    }
}
