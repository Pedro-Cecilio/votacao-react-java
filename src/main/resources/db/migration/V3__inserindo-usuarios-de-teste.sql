-- Inserir um novo usuário administrador
INSERT INTO usuario (nome, sobrenome, cpf, admin)
VALUES ('Admin', 'Tester', '12345678911', TRUE);
INSERT INTO usuario (nome, sobrenome, cpf, admin)
VALUES ('Usuario', 'Tester', '12345678912', FALSE);

INSERT INTO autenticacao (email, senha, usuario_id)
VALUES ('restassuredAdmin@email.com', '$2a$10$2dbA9zTcOkI0UTnX4CyE3uQCYZd/FqWaF7pvxRXkj9/TPiMwZAEmu', (SELECT id FROM usuario WHERE cpf = '12345678911'));
INSERT INTO autenticacao (email, senha, usuario_id)
VALUES ('restassuredUsuario@email.com', '$2a$10$2dbA9zTcOkI0UTnX4CyE3uQCYZd/FqWaF7pvxRXkj9/TPiMwZAEmu', (SELECT id FROM usuario WHERE cpf = '12345678912'));
