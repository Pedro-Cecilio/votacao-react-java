package com.dbserver.votacaoBackend.domain.autenticacao.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.domain.autenticacao.Autenticacao;
import com.dbserver.votacaoBackend.domain.autenticacao.repository.AutenticacaoRepository;
import com.dbserver.votacaoBackend.domain.usuario.Usuario;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {
    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Mock 
    private AutenticacaoRepository autenticacaoRepository;

    public AutenticacaoServiceTest() {

    }
    private Usuario usuarioMock;
    private Autenticacao autenticacaoMock;

    @BeforeEach
    void configurar(){
        this.usuarioMock = new Usuario(1L, "João", "Silva", "12345678900", false);
        this.autenticacaoMock = new Autenticacao("example@example.com", "senha123");
    }
    @AfterEach
    void limpar(){
        this.usuarioMock = null;
        this.autenticacaoMock = null;
        this.autenticacaoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve ser possível criar uma autenticacao")
    void givenTenhoUmUsuarioCadastradoEDadosDeAutenticacaoWhenTentoCriarAutenticacaoThenRetornarAutenticacaoCriada(){
        when(this.autenticacaoRepository.save(this.autenticacaoMock)).thenReturn(this.autenticacaoMock);
        this.autenticacaoService.criarAutenticacao(autenticacaoMock, usuarioMock);
        verify(this.autenticacaoRepository, times(1)).save(this.autenticacaoMock);
    }
    @Test
    @DisplayName("Deve ser possível deletar uma autenticacao")
    void givenTenhoUmaAutenticacaoIdWhenTentoDeletarAutenticacaoThenDeletarAutenticacao(){
        when(this.autenticacaoRepository.findById(1L)).thenReturn(Optional.of(this.autenticacaoMock));
        this.autenticacaoService.deletarAutenticacao(1L);
        verify(this.autenticacaoRepository, times(1)).delete(this.autenticacaoMock);
    }
    @Test
    @DisplayName("Não deve ser possível deletar uma autenticacao inexistente")
    void givenTenhoUmaAutenticacaoIdInexistenteWhenTentoDeletarAutenticacaoThenRetornarUmaExcecao(){
        when(this.autenticacaoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, ()->this.autenticacaoService.deletarAutenticacao(1L));
        
    }
}
