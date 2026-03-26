package com.weg.oficina.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ordem_servico")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String equipamento;

    @Column
    private String defeitoRelatado;

    @Column
    private String status;

    @Column
    private String materialUsados;

    @Column
    private String laudoTecnico;

    @ManyToOne
    private Professor professor;

    @ManyToMany
    @JoinTable(
            name = "os_executores",
            joinColumns = @JoinColumn(name = "os_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private List<Aluno> executores = new ArrayList<>();

}
