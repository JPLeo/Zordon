require('dotenv').config();

const mysql = require("mysql2");


const conexao = mysql.createConnection({
    host:  process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DATABASE,
    port: process.env.DB_PORT
});

conexao.connect((erro) => {
    if (erro) {
        console.log("Erro ao conectar:", erro);
        throw new Error("Erro ao conectar:", erro.message);
        
    } else {
        console.log("MySQL conectado");
    }
});

module.exports = conexao;