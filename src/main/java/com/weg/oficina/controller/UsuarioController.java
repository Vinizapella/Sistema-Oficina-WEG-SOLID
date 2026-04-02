package com.weg.oficina.controller;

import com.weg.oficina.dto.UsuarioRequestDto;
import com.weg.oficina.dto.UsuarioResponseDto;
import com.weg.oficina.service.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService iUsuarioService;

    public UsuarioController(
            IUsuarioService iUsuarioService
    ) {
        this.iUsuarioService = iUsuarioService;
    }

    @PostMapping
    public UsuarioResponseDto criar(
            @RequestBody @Valid UsuarioRequestDto usuarioRequestDto
    ){
        return iUsuarioService.salvar(usuarioRequestDto);
    }

    @GetMapping
    List<UsuarioResponseDto> listar(){
        return iUsuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDto buscarId(
            @PathVariable Long id
    ){
        return iUsuarioService.buscarPorId(id);
    }

}
