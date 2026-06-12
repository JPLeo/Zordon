const sqlite3 = require('sqlite3').verbose();
const path = require('path');

// Cria ou conecta ao arquivo do banco na pasta backend-node
const dbPath = path.resolve(__dirname, 'arcade_poo.db');

const conexao = new sqlite3.Database(dbPath, (erro) => {
    if (erro) {
        console.log("Erro ao conectar no SQLite:", erro.message);
    } else {
        console.log("SQLite conectado com sucesso! (Arquivo: arcade_poo.db)");
        criarTabelas();
    }
});

// Garante que a estrutura das tabelas está criada perfeitamente
function criarTabelas() {
    conexao.serialize(() => {
        conexao.run(`
            CREATE TABLE IF NOT EXISTS usuario (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                pontuacao_total INTEGER NOT NULL DEFAULT 0
            )
        `);

        conexao.run(`
            CREATE TABLE IF NOT EXISTS jogo (
                id_jogo INTEGER PRIMARY KEY AUTOINCREMENT,
                nome_jogo TEXT NOT NULL,
                categoria TEXT NOT NULL,
                dificuldade TEXT NOT NULL
            )
        `);

        conexao.run(`
            CREATE TABLE IF NOT EXISTS partida (
                id_partida INTEGER PRIMARY KEY AUTOINCREMENT,
                id_usuario INTEGER NOT NULL,
                id_jogo INTEGER NOT NULL,
                resultado INTEGER NOT NULL,
                modo_jogo TEXT NOT NULL,
                pontuacao INTEGER NOT NULL DEFAULT 0,
                data_partida TEXT NOT NULL DEFAULT (CURRENT_DATE),
                FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
                FOREIGN KEY (id_jogo) REFERENCES jogo (id_jogo)
            )
        `, (erro) => {
            if (erro) {
                console.log("Erro ao estruturar as tabelas:", erro.message);
            } else {
                console.log("Estrutura do banco de dados verificada/criada com sucesso!");
            }
        });
    });
}

// 🎯 O TRADUTOR: Transforma as chamadas de db.query() do MySQL para o padrão do SQLite
conexao.query = function (sql, params, callback) {
    // Se o código passar só a query e a função de callback (sem parâmetros de busca)
    if (typeof params === 'function') {
        callback = params;
        params = [];
    }
    // O comando "??" no MySQL não existe no SQLite, alteramos para "?" se necessário
    let sqlFormatado = sql.replace(/\?\?/g, '?');

    // Executa a busca no formato do SQLite
    conexao.all(sqlFormatado, params, (erro, linhas) => {
        if (callback) {
            // O MySQL retorna (erro, resultados), o SQLite retorna (erro, linhas)
            callback(erro, linhas);
        }
    });
};

// Adaptação caso o código chame o método connect
conexao.connect = (callback) => { if (callback) callback(null); };

module.exports = conexao;