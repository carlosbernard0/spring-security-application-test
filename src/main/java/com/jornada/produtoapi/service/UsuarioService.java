package com.jornada.produtoapi.service;

import com.jornada.produtoapi.dto.AutenticacaoDTO;
import com.jornada.produtoapi.entity.UsuarioEntity;
import com.jornada.produtoapi.exceptions.BusinessException;
import com.jornada.produtoapi.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static io.jsonwebtoken.Jwts.parser;

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

        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(tokenLimpo) //decriptograda e valida o token VALIDAÇÃO AQUI
                .getBody(); //recuperar o payload

        String subject = claims.getSubject(); //ID usuario

        Optional<UsuarioEntity> usuarioEntityOptional= usuarioRepository.findById(Integer.parseInt(subject));
        return usuarioEntityOptional.orElseThrow(()-> new BusinessException("Usuario e senha inválidos"));
    }
}
