package com.jornada.produtoapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "USUARIOC")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "USUARIO_SEQUENCIA")
    @SequenceGenerator(name ="USUARIO_SEQUENCIA", sequenceName = "seq_usuarioc", allocationSize = 1)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "login")
    private String login;

    @Column(name = "senha")
    private String senha;
}
