CREATE DATABASE IF NOT EXISTS plataforma_jogos
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE plataforma_jogos;

-- Usuario.java: idUsuario, nome, pontuacaoTotal
CREATE TABLE usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  pontuacao_total INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Jogo.java: idJogo, nomeJogo, categoria, dificuldade
CREATE TABLE jogo (
  id_jogo INT NOT NULL AUTO_INCREMENT,
  nome_jogo VARCHAR(100) NOT NULL,
  categoria VARCHAR(100) NOT NULL,
  dificuldade VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_jogo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Partida.java: idPartida, usuario, jogo, resultado, modoJogo, pontuacao, dataPartida
CREATE TABLE partida (
  id_partida INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_jogo INT NOT NULL,
  resultado INT NOT NULL,
  modo_jogo VARCHAR(100) NOT NULL,
  pontuacao INT NOT NULL DEFAULT 0,
  data_partida DATE NOT NULL DEFAULT (CURRENT_DATE),
  PRIMARY KEY (id_partida),
  CONSTRAINT fk_partida_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
  CONSTRAINT fk_partida_jogo
    FOREIGN KEY (id_jogo) REFERENCES jogo (id_jogo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
