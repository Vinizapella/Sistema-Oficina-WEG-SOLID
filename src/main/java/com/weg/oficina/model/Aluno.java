package com.weg.oficina.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@EqualsAndHashCode(callSuper = true)
public class Aluno extends Usuario{

    @Column
    private String matricula;

}
