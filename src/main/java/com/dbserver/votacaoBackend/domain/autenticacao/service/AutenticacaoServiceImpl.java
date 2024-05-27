package com.dbserver.votacaoBackend.domain.autenticacao.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutenticacaoRespostaDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoDto;
import com.dbserver.votacaoBackend.domain.autenticacao.dto.AutorizarVotoExternoRespostaDto;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.autenticacao.validacoes.AutenticacaoValidacoes;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.domain.usuario.validacoes.UsuarioValidacoes;
import com.dbserver.votacaoBackend.infra.security.token.TokenService;

@Service
public class AutenticacaoServiceImpl implements AutenticacaoService {
    private PasswordEncoder passwordEncoder;
    private AutenticacaoRepository autenticacaoRepository;
    private TokenService tokenService;
    private AutenticacaoValidacoes autenticacaoValidacoes;

    public AutenticacaoServiceImpl(AutenticacaoRepository autenticacaoRepository, PasswordEncoder passwordEncoder,
            TokenService tokenService, AutenticacaoValidacoes autenticacaoValidacoes) {
        this.autenticacaoRepository = autenticacaoRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.autenticacaoValidacoes = autenticacaoValidacoes;
    }

    @Override
    public AutenticacaoRespostaDto autenticarUsuario(AutenticacaoDto dto) {
        Autenticacao autenticacao = this.autenticacaoRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BadCredentialsException("Dados de login inválidos."));

        boolean senhaValida = this.autenticacaoValidacoes.validarSenhaDaAutenticacao(dto.senha(),
                autenticacao.getSenha());
        if (!senhaValida) {
            throw new BadCredentialsException("Dados de login inválidos.");
        }

        String token = this.tokenService.gerarToken(autenticacao);
        return new AutenticacaoRespostaDto(token, autenticacao.getUsuario().isAdmin());
    }

    @Override
    public AutorizarVotoExternoRespostaDto autorizarUsuarioVotoExterno(AutorizarVotoExternoDto dto) {
        this.autenticacaoValidacoes.validarAutenticacaoPorCpfESenha(dto.cpf(), dto.senha());
        return new AutorizarVotoExternoRespostaDto(true);
    }

    @Override
    public Autenticacao criarAutenticacao(Autenticacao autenticacao, Usuario usuarioSalvo) {
        UsuarioValidacoes.validarUsuarioNaoNulo(usuarioSalvo);
        autenticacaoValidacoes.validarAutenticacaoNaoNula(autenticacao);

        autenticacao.setUsuario(usuarioSalvo);

        return this.autenticacaoRepository.save(autenticacao);
    }

    @Override
    public String encriptarSenhaDaAutenticacao(String senha) {
        AutenticacaoValidacoes.validarFormatoDaSenha(senha);
        return this.passwordEncoder.encode(senha);
    }

    @Override
    public boolean verificarEmailJaEstaCadastrado(String email) {
        return this.autenticacaoRepository.findByEmail(email).isPresent();
    }

}
