package com.dbserver.votacaoBackend.utils;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Utils {
    private PasswordEncoder passwordEncoder;

    @Autowired
    public Utils(PasswordEncoder passwordeEncoder){
        this.passwordEncoder = passwordeEncoder;
    }

    public Utils(){
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    public static final String REGEX_CPF = "\\d{11}";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static boolean validarRegex(String regex, String conteudo) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(conteudo).matches();
    }
    public String encriptarSenha(String senha){
        return this.passwordEncoder.encode(senha);
    }

    public boolean validarSenha(String senhaEsperada, String senhaEncriptada){
        return this.passwordEncoder.matches(senhaEsperada, senhaEncriptada);
    }

}
