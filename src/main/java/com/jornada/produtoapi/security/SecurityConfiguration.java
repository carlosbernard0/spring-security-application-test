package com.jornada.produtoapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean //filtragem das requisições
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //permissoes e filtros
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests((authz)->
//                authz.anyRequest().permitAll() // todos os endpoints podem ser acessados
                    authz.anyRequest().authenticated()
        );

        return http.build();
    }
}
