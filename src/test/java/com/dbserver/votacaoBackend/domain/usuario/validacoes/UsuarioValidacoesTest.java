package com.dbserver.votacaoBackend.domain.usuario.validacoes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.dbserver.votacaoBackend.domain.usuario.Usuario;
import com.dbserver.votacaoBackend.fixture.usuario.UsuarioFixture;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UsuarioValidacoesTest {

    @InjectMocks
    private UsuarioValidacoes usuarioValidacoes;
    @Mock
    private Usuario usuarioMock;

    @Test
    @DisplayName("Deve validar ao passar usuário não nulo")
    void dadoUsuarioNaoNuloQuandoTentoValidarSeNaoENuloDeveValidarCorretamente() {
        assertDoesNotThrow(() -> this.usuarioValidacoes.validarUsuarioNaoNulo(usuarioMock));
    }

    @Test
    @DisplayName("Deve retornar erro ao validar ao passar usuário nulo")
    void dadoUsuarioNuloQuandoTentoValidarSeNaoENuloDeveRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> this.usuarioValidacoes.validarUsuarioNaoNulo(null));
    }

    @Test
    @DisplayName("Deve validar ao passar nome válido")
    void dadoNomeValidoQuandoValidoFormatoDeveValidarCorretamente() {
        assertDoesNotThrow(() -> UsuarioValidacoes.validarformatoNome("Lucas"));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar nome nulo")
    void dadoNomeNuloQuandoValidoFormatoDeveRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> UsuarioValidacoes.validarformatoNome(null));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar nome vazio")
    void dadoNomeVazioQuandoValidoFormatoDeveRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> UsuarioValidacoes.validarformatoNome(""));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar nome curto")
    void dadoNomeCurtoQuandoValidoFormatoDeveRetornarErro() {
        assertThrows(IllegalArgumentException.class, () -> UsuarioValidacoes.validarformatoNome("Lu"));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar nome longo")
    void dadoNomeLongoQuandoValidoFormatoDeveRetornarErro() {
        assertThrows(IllegalArgumentException.class,
                () -> UsuarioValidacoes.validarformatoNome("LucasLucasLucasLucasL"));
    }

    @Test
    @DisplayName("Deve validar ao passar sobrenome válido")
    void dadoSobrenomeValidoQuandoValidoFormatoDeveValidarCorretamente(){
        assertDoesNotThrow(() -> UsuarioValidacoes.validarFormatoSobrenome("Silva"));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar sobrenome nulo")
    void dadoSobrenomeNuloQuandoValidoFormatoDeveRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> UsuarioValidacoes.validarFormatoSobrenome(null));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar sobrenome vazio")
    void dadoSobrenomeVazioQuandoValidoFormatoDeveRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> UsuarioValidacoes.validarFormatoSobrenome(""));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar sobrenome curto")
    void dadoSobrenomeCurtoQuandoValidoFormatoDeveRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> UsuarioValidacoes.validarFormatoSobrenome("S"));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar sobrenome longo")
    void dadoSobrenomeLongoQuandoValidoFormatoDeveRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> UsuarioValidacoes.validarFormatoSobrenome("SilvaSilvaSilvaSilvaa"));
    }

    @Test
    @DisplayName("Deve validar ao passar CPF válido")
    void dadoCpfValidoQuandoValidoFormatoDeveValidarCorretamente(){
        assertDoesNotThrow(() -> UsuarioValidacoes.validarFormatoCpf(UsuarioFixture.CPF_ALEATORIO));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar CPF nulo")
    void dadoCpfNuloQuandoValidoFormatoDeveRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> UsuarioValidacoes.validarFormatoCpf(null));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar CPF inválido")
    void dadoCpfInvalidoQuandoValidoFormatoDeveRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> UsuarioValidacoes.validarFormatoCpf(UsuarioFixture.CPF_INVALIDO));
    }

    @Test
    @DisplayName("Deve retornar erro ao passar CPF com caracteres não numéricos")
    void dadoCpfComCaracteresNaoNumericosQuandoValidoFormatoDeveRetornarErro(){
        assertThrows(IllegalArgumentException.class, () -> UsuarioValidacoes.validarFormatoCpf("1234567890a"));
    }
}




