package com.dbserver.votacaoBackend.infra.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.infra.exeptions.novas.RespostaErro;
import com.dbserver.votacaoBackend.infra.exeptions.novas.ValidarJwtExeption;
import com.dbserver.votacaoBackend.infra.security.token.TokenService;
import com.dbserver.votacaoBackend.infra.security.userDetails.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private TokenService tokenService;
    private AutenticacaoRepository autenticacaoRepository;

    public SecurityFilter(TokenService tokenService, AutenticacaoRepository autenticacaoRepository) {
        this.tokenService = tokenService;
        this.autenticacaoRepository = autenticacaoRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.recuperarToken(request);

        if (token != null) {
            try {
                String email = tokenService.validarToken(token);

                Optional<Autenticacao> autenticacao = this.autenticacaoRepository.findByEmail(email);
                
                if (autenticacao.isPresent()) {
                    Autenticacao autenticacaoEncontrada = autenticacao.get();
                    UserDetailsImpl userDetailsImpl = new UserDetailsImpl(autenticacaoEncontrada);

                    var authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl.getUsuario(), null,
                    userDetailsImpl.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    this.respostaErro(response);
                    return;
                }
            } catch (ValidarJwtExeption e) {
                this.respostaErro(response);
                return;
            }
        }
        filterChain.doFilter(request, response);

    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }

    public void respostaErro(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RespostaErro responseError = new RespostaErro("Token inválido");
        String tokenErrorResponse = mapper.writeValueAsString(responseError);

        response.setStatus(401);
        response.addHeader("Content-type", "application/json");
        response.getWriter().write(tokenErrorResponse);
    }
}