package com.weg.oficina.service;

import com.weg.oficina.dto.UsuarioRequestDto;
import com.weg.oficina.dto.UsuarioResponseDto;
import com.weg.oficina.mapper.UsuarioMapper;
import com.weg.oficina.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements IUsuarioService{

    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UsuarioResponseDto salvar(
            UsuarioRequestDto usuarioRequestDto
    ) {
        var entity = usuarioMapper.toEntity(usuarioRequestDto);
        var salvo = usuarioRepository.save(entity);
        return usuarioMapper.toResponse(salvo);
    }

    @Override
    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    @Override
    public UsuarioResponseDto buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
