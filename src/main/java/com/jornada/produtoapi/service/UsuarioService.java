package com.jornada.produtoapi.service;

import com.jornada.produtoapi.dto.AutenticacaoDTO;
import com.jornada.produtoapi.entity.UsuarioEntity;
import com.jornada.produtoapi.exceptions.BusinessException;
import com.jornada.produtoapi.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Value("${jwt.validade.token}")
    private String validadeJWT;

    @Value("${jwt.secret}")
    private String secret;

    public String fazerLogin(AutenticacaoDTO autenticacaoDTO) throws BusinessException {
        Optional<UsuarioEntity> usuarioEntityOptional =usuarioRepository.findByLoginAndSenha(autenticacaoDTO.getLogin(), autenticacaoDTO.getSenha());
        if (usuarioEntityOptional.isEmpty()){
            throw new BusinessException("Usuario e senha inválidos");
        }
        UsuarioEntity usuario = usuarioEntityOptional.get();
//        String tokenGerado = usuario.getLogin() + "-" + usuario.getSenha();
//        return tokenGerado;

        Date dataAtual = new Date();
        Date dataExpiracao = new Date(dataAtual.getTime()+Long.parseLong(validadeJWT));


        String jwtGerado = Jwts.builder()
                .setIssuer("produto-api")
                .setSubject(usuario.getIdUsuario().toString())
                .setIssuedAt(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();

        return jwtGerado;
    }

    // token = null
    // token = Bearer <TOKEN>
    public UsuarioEntity validarToken(String token) throws BusinessException {
        if (token == null){
            throw new BusinessException("Token inexistente");
        }

        String tokenLimpo = token.replace("Bearer ", ""); //<TOKEN> (USUARIO-SENHA)
        String[] usuarioESenha = tokenLimpo.split("-"); //[0] =USUARIO, [1] =SENHA
        Optional<UsuarioEntity> usuarioEntityOptional= usuarioRepository.findByLoginAndSenha(usuarioESenha[0], usuarioESenha[1]);
        return usuarioEntityOptional.orElseThrow(()-> new BusinessException("Usuario e senha inválidos"));
    }
}
