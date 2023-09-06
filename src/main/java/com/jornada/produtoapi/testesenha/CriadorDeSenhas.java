package com.jornada.produtoapi.testesenha;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriadorDeSenhas {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//        String senhaCriptografada = bCryptPasswordEncoder.encode("senha123");
//        System.out.println(senhaCriptografada);
//        $2a$10$Q87WnwzwSQVUMv2lyTtdKumx.MpPMghXlmVtK4yS5CVt3mUWSI9S6


        boolean senhaCorreta =bCryptPasswordEncoder.matches("123", "$2a$10$Q87WnwzwSQVUMv2lyTtdKumx.MpPMghXlmVtK4yS5CVt3mUWSI9S6");
            System.out.println(senhaCorreta);
    }
}   
