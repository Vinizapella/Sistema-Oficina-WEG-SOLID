package com.weg.oficina.repository;

import com.weg.oficina.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    List<OrdemServico> findAllByProfessorId (Long professorId);

    List<OrdemServico> findAllByStatus(String status);

}
