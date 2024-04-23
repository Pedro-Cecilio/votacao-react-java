package com.dbserver.votacaoBackend.utils;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.dbserver.votacaoBackend.domain.pauta.Pauta;
import com.dbserver.votacaoBackend.domain.pauta.dto.RespostaPautaDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.dto.RespostaSessaoVotacaoDto;
import com.dbserver.votacaoBackend.domain.sessaoVotacao.service.SessaoVotacaoService;

@Component
public class Utils {
    private SessaoVotacaoService sessaoVotacaoService;

    public Utils(SessaoVotacaoService sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    public static final String REGEX_CPF = "\\d{11}";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static boolean validarRegex(String regex, String conteudo) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(conteudo).matches();
    }

    public List<RespostaPautaDto> criarListaRespostaPautaDto(List<Pauta> pautas) {
        return pautas.stream().map(pauta -> {
            if (pauta.getSessaoVotacao() == null) {
                return new RespostaPautaDto(pauta, null);
            }
            boolean sessaoEstaAtiva = this.sessaoVotacaoService.verificarSeSessaoVotacaoEstaAtiva(pauta.getSessaoVotacao());
            return new RespostaPautaDto(pauta, new RespostaSessaoVotacaoDto(pauta.getSessaoVotacao(), sessaoEstaAtiva));
        }).toList();
    }
}
