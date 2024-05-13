package com.dbserver.votacaoBackend.utils;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class Utils {

    public static final String REGEX_CPF = "\\d{11}";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static boolean validarRegex(String regex, String conteudo) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(conteudo).matches();
    }

    public LocalDateTime obterDataAtual(){
        return LocalDateTime.now();
    }
}
