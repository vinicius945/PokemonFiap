-- Exclui as tabelas (caso existam)
DROP TABLE IF EXISTS pokemon;
DROP TABLE IF EXISTS treinador;

-- Criação da tabela treinador
CREATE TABLE treinador (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Criação da tabela pokemon com FK
CREATE TABLE pokemon (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    nivel INT NOT NULL CHECK (nivel BETWEEN 1 AND 100),
    data_captura DATE,
    treinador_id BIGINT NOT NULL,
    CONSTRAINT fk_treinador FOREIGN KEY (treinador_id)
        REFERENCES treinador(id)
        ON DELETE CASCADE
);
