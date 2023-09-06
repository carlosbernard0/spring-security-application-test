package com.jornada.produtoapi.security;


import com.jornada.produtoapi.entity.UsuarioEntity;
import com.jornada.produtoapi.exceptions.BusinessException;
import com.jornada.produtoapi.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    public final UsuarioService usuarioService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //recuperar verificar o token da request
        String tokenBearer = request.getHeader("Authorization");
        //validar o token
        try {
            UsuarioEntity usuario = usuarioService.validarToken(tokenBearer);
            //usuario existe e deu tudo certo com o token
            UsernamePasswordAuthenticationToken tokenSpring =
                    new UsernamePasswordAuthenticationToken(usuario, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(tokenSpring); // estou logado...
        } catch (BusinessException e) {
            //usuario nao existe
            SecurityContextHolder.getContext().setAuthentication(null); // não estou logado...

        }

        //executa o proximo filtro
        filterChain.doFilter(request, response);
    }
}
