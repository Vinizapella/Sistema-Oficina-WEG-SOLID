package com.weg.oficina.service;


import com.weg.oficina.dto.UsuarioRequestDto;
import com.weg.oficina.dto.UsuarioResponseDto;

import java.util.List;

public interface IUsuarioService {

    UsuarioResponseDto salvar(
            UsuarioRequestDto usuarioRequestDto
    );

    List<UsuarioResponseDto> listarTodos();

    UsuarioResponseDto buscarPorId(
            Long id
    );

}
