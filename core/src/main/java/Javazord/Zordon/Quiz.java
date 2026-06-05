package Javazord.Zordon;

import java.util.ArrayList;
import java.util.Collections;

public class Quiz
{
    private ArrayList<Pergunta> perguntas = new ArrayList<>();
    private int pontuacao = 0;

    public Quiz()
    {
        perguntas.add(new Pergunta("Qual o maior planeta do sistema solar?", "Jupiter",
            "Marte", "Terra",
            "Saturno", 'a'));

        perguntas.add(new Pergunta("Quem pintou a obra Mona Lisa?", "Pablo Picasso",
            "Leonardo da Vinci", "Vincent van Gogh",
            "Michelangelo", 'b'));

        perguntas.add(new Pergunta("Quantos continentes existem no planeta Terra?", "5",
            "6", "7",
            "8", 'c'));

        perguntas.add(new Pergunta("Qual elemento quimico e representado pelo simbolo 'O'?", "Ouro",
            "Oxigenio", "Osmio",
            "Prata", 'b'));

        perguntas.add(new Pergunta("Qual foi o primeiro presidente do Brasil?", "Getulio Vargas",
            "Juscelino Kubitschek", "Deodoro da Fonseca",
            "Fernando Collor", 'c'));

        perguntas.add(new Pergunta("Qual o maior animal terrestre do mundo?", "Girafa",
            "Hipopotamo", "Rinoceronte",
            "Elefante-africano", 'd'));

        perguntas.add(new Pergunta("Qual e a metade de 100?", "50",
            "40", "25",
            "60", 'a'));

        perguntas.add(new Pergunta("Quanto e 8 x 7?", "54",
            "64", "48",
            "56", 'd'));

        perguntas.add(new Pergunta("Quem descobriu o Brasil segundo a historia tradicional?", "Pedro Alvares Cabral",
            "Cristovao Colombo", "Vasco da Gama",
            "Tiradentes", 'a'));

        perguntas.add(new Pergunta("Qual e a capital do Brasil?", "Sao Paulo",
            "Rio de Janeiro", "Brasilia",
            "Belo Horizonte", 'c'));
    }

    public void sortearPerguntas()
    {
        Collections.shuffle(perguntas);
    }

    public Pergunta getPergunta(int i)
    {
        return perguntas.get(i);
    }

    public boolean verificarResposta(int i, char respostaUsuario)
    {
        Pergunta pergunta = perguntas.get(i);
        return pergunta.conferePergunta(Character.toLowerCase(respostaUsuario));
    }

    public void adicionaPontuacao()
    {
        pontuacao = pontuacao + 10;
    }

    public int getPontuacao()
    {
        return pontuacao;
    }

    public int getQuantidadePerguntasPartida()
    {
        return 5;
    }
}