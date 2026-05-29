const express = require("express");
const db = require("./db");

const app = express();

app.use(express.json());

app.get("/", (req, res) => {
    res.send("Servidor funcionando!");
});

app.post("/usuarios/entrar", (req, res) => {
    const nome = typeof req.body.nome === "string" ? req.body.nome.trim() : "";

    if (!nome) {
        return res.status(400).json({ erro: "Informe o nome do jogador." });
    }

    if (nome.length > 100) {
        return res.status(400).json({ erro: "Nome muito longo (maximo 100 caracteres)." });
    }

    db.query(
        "SELECT id_usuario, nome, pontuacao_total FROM usuario WHERE nome = ? LIMIT 1",
        [nome],
        (erro, linhas) => {
            if (erro) {
                console.error(erro);
                return res.status(500).json({ erro: "Erro ao consultar o banco." });
            }

            if (linhas.length > 0) {
                const usuario = linhas[0];
                return res.json({
                    idUsuario: usuario.id_usuario,
                    nome: usuario.nome,
                    pontuacaoTotal: usuario.pontuacao_total,
                });
            }

            db.query(
                "INSERT INTO usuario (nome) VALUES (?)",
                [nome],
                (erroInsert, resultado) => {
                    if (erroInsert) {
                        console.error(erroInsert);
                        return res.status(500).json({ erro: "Erro ao salvar jogador." });
                    }

                    res.status(201).json({
                        idUsuario: resultado.insertId,
                        nome,
                        pontuacaoTotal: 0,
                    });
                }
            );
        }
    );
});

app.listen(3000, () => {
    console.log("Servidor rodando em http://localhost:3000");
});
