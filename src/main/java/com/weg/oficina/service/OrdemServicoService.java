package com.weg.oficina.service;

import com.weg.oficina.dto.ordemServico.OrdemServicoAtualizarDto;
import com.weg.oficina.dto.ordemServico.OrdemServicoRequestDto;
import com.weg.oficina.dto.ordemServico.OrdemServicoResponseDto;
import com.weg.oficina.mapper.OrdemServicoMapper;
import com.weg.oficina.model.Aluno;
import com.weg.oficina.model.OrdemServico;
import com.weg.oficina.model.Professor;
import com.weg.oficina.repository.OrdemServicoRepository;
import com.weg.oficina.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrdemServicoService implements IOrdemServicoService{

    private final OrdemServicoRepository ordemServicoRepository;

    private final OrdemServicoMapper ordemServicoMapper;

    private final UsuarioRepository usuarioRepository;

    public OrdemServicoService(
            OrdemServicoRepository ordemServicoRepository,
            OrdemServicoMapper ordemServicoMapper,
            UsuarioRepository usuarioRepository
    ) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.ordemServicoMapper = ordemServicoMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public OrdemServicoResponseDto abrir(
            OrdemServicoRequestDto ordemServicoRequestDto
    ) {
        var usuario = usuarioRepository.findById(ordemServicoRequestDto.professorId())
                .orElseThrow(()->new RuntimeException("Professor não encontrado"));

        if (!(usuario instanceof Professor professor)){
            throw new RuntimeException("Acesso negado. somente o professor responsavel pode encerrar esta Ordem de Serviço");
        }

        var alunos = usuarioRepository.findAllById(ordemServicoRequestDto.alunosIds())
                .stream()
                .map(u -> (Aluno) u)
                .toList();

        var os = ordemServicoMapper.toEntity(ordemServicoRequestDto, professor, alunos);

        return ordemServicoMapper.toResponse(ordemServicoRepository.save(os));
    }

    @Override
    @Transactional
    public void atualizarStatus(
            Long id,
            OrdemServicoAtualizarDto ordemServicoAtualizarDto
    ) {
        OrdemServico os = ordemServicoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Ordem de serviço não encontrada"));

        if (!os.getProfessor().getId().equals(ordemServicoAtualizarDto.professorId())){
            throw new RuntimeException("Acesso negado. somente o professor responsavel pode encerrar esta Ordem de Serviço");
        }

        os.setStatus(ordemServicoAtualizarDto.novoStatus());
        os.setLaudoTecnico(ordemServicoAtualizarDto.laudo());
        os.setMaterialUsados(ordemServicoAtualizarDto.materiais());
        ordemServicoRepository.save(os);
    }
}
