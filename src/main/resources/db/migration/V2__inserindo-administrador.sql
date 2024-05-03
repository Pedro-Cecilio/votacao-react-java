-- Inserir um novo usuário administrador
INSERT INTO usuario (nome, sobrenome, cpf, admin)
VALUES ('Roger', 'Siqueira', '12345678910', TRUE);

INSERT INTO autenticacao (email, senha, usuario_id)
VALUES ('admin@email.com', '$2a$10$rrGF4eS0uqs7Ip0/pW/thehyNkKBfmV3NqwY2TljAH9egMQ49MATa', (SELECT id FROM usuario WHERE cpf = '12345678910'));
