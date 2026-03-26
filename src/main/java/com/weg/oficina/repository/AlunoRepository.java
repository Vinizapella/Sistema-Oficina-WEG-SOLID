package com.weg.oficina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<com.weg.oficina.model.Aluno, Long> {

    Optional<com.weg.oficina.model.Aluno> findByMatricula(String matricula);

}
