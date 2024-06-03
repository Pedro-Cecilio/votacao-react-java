package com.dbserver.votacaoBackend.domain.usuario.validacoes;

import org.springframework.stereotype.Component;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.utils.Utils;

@Component
public class UsuarioValidacoes {

    public static void validarformatoNome(String nome) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("Nome deve ser informado.");

        if (nome.trim().length() < 3 || nome.trim().length() > 20)
            throw new IllegalArgumentException("Nome deve conter entre 3 e 20 caracteres.");
    }

    public static void validarFormatoSobrenome(String sobrenome) {
        if (sobrenome == null || sobrenome.trim().isEmpty())
            throw new IllegalArgumentException("Sobrenome deve ser informado.");

        if (sobrenome.trim().length() < 2 || sobrenome.trim().length() > 20)
            throw new IllegalArgumentException("Sobrenome deve conter entre 2 e 20 caracteres.");
    }

    public static void validarFormatoCpf(String cpf) {
        if (cpf == null || !Utils.validarRegex(Utils.REGEX_CPF, cpf.trim()))
            throw new IllegalArgumentException("Cpf deve conter 11 caracteres numéricos.");
    }

    public static void validarUsuarioNaoNulo(Usuario usuario) {
        if (usuario == null)
            throw new IllegalArgumentException("Usuario deve ser informado.");
    }

}
