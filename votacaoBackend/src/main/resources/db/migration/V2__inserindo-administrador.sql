-- Inserir um novo usuário administrador
INSERT INTO usuario (nome, sobrenome, cpf, admin)
VALUES ('Roger', 'Siqueira', '12345678910', TRUE);

INSERT INTO autenticacao (email, senha, usuario_id)
VALUES ('admin@email.com', '$2a$10$l2K0VLs6EU9LJjbqYlnAFuogcGTstmecLPu4RpZpd6DopKuHt/Ew2
', (SELECT id FROM usuario WHERE cpf = '12345678910'));
