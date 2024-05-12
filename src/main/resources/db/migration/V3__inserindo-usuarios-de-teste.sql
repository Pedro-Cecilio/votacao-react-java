-- Inserir um novo usuário administrador
INSERT INTO usuario (nome, sobrenome, cpf, admin)
VALUES ('Admin', 'Tester', '12345678911', TRUE);
INSERT INTO usuario (nome, sobrenome, cpf, admin)
VALUES ('Usuario', 'Tester', '12345678912', FALSE);

INSERT INTO autenticacao (email, senha, usuario_id)
VALUES ('testesAdmin@email.com', '$2a$10$Y9dFckNplor4dLJzmuDZ.OFOi0HAF5tdjnqJ.DzaBegrvXciTWrbm', (SELECT id FROM usuario WHERE cpf = '12345678911'));
INSERT INTO autenticacao (email, senha, usuario_id)
VALUES ('testesUsuario@email.com', '$2a$10$Y9dFckNplor4dLJzmuDZ.OFOi0HAF5tdjnqJ.DzaBegrvXciTWrbm', (SELECT id FROM usuario WHERE cpf = '12345678912'));
