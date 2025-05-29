
CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100),
    nome_usuario VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL
);
CREATE TABLE contato IF EXISTS usuario (



)
