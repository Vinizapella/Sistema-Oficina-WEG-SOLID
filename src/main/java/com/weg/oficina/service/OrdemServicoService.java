package com.weg.oficina.service;

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
        var professor = (Professor) usuarioRepository.findById(ordemServicoRequestDto.professorId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

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
            String novoStatus,
            String laudo,
            String materiais
    ) {
        OrdemServico os = ordemServicoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Ordem de serviço não encontrada"));
        os.setStatus(novoStatus);
        os.setLaudoTecnico(laudo);
        os.setMaterialUsados(materiais);
        ordemServicoRepository.save(os);
    }
}
