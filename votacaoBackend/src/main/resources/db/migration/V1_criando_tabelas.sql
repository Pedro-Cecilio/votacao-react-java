CREATE TABLE usuario(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(20) NOT NULL,
    sobrenome VARCHAR(20) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    admin BOOLEAN NOT NULL
)

CREATE TABLE autenticacao(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    usuario_id BIGINT UNIQUE NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
)

CREATE TABLE voto(
    id BIGSERIAL PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL,
    usuario_id BIGINT ,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
)


CREATE TABLE sessaoVotacao(
    id BIGSERIAL PRIMARY KEY,
    data_abertura TIMESTAMP WITH TIME ZONE,
    data_fechamento TIMESTAMP WITH TIME ZONE,
)

CREATE TABLE sessao_votacao_votos_positivos(
    sessao_votacao_id BIGINT,
    votos_positivos_id BIGINT,

    FOREIGN KEY (sessao_votacao_id) REFERENCES sessaoVotacao(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (votos_positivos_id) REFERENCES voto(id) ON DELETE CASCADE ON UPDATE CASCADE
)
CREATE TABLE sessao_votacao_votos_negativos(
    sessao_votacao_id BIGINT,
    votos_negativos_id BIGINT,

    FOREIGN KEY (sessao_votacao_id) REFERENCES sessaoVotacao(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (votos_negativos_id) REFERENCES voto(id) ON DELETE CASCADE ON UPDATE CASCADE
)

CREATE TABLE pauta(
    id BIGSERIAL PRIMARY KEY,
    assunto VARCHAR(255) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
)

ALTER TABLE sessao_votacao
ADD CONSTRAINT fk_sessao_votacao_pauta
FOREIGN KEY (pauta_id) REFERENCES sessao_votacao(id) ON UPDATE CASCADE