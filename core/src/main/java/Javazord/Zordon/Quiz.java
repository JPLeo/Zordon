package Javazord.Zordon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Collections;

public class Quiz
{
    private static final String CAMINHO_PERGUNTAS = "Quiz/perguntas.json";
    private static final int QUANTIDADE_PADRAO_PARTIDA = 5;

    private ArrayList<Pergunta> perguntas = new ArrayList<>();
    private int quantidadePerguntasPartida = QUANTIDADE_PADRAO_PARTIDA;
    private int pontuacao = 0;

    public Quiz()
    {
        if (!carregarPerguntasJson())
        {
            carregarPerguntasPadrao();
        }
    }

    private boolean carregarPerguntasJson()
    {
        try
        {
            FileHandle arquivo = Gdx.files.internal(CAMINHO_PERGUNTAS);
            if (!arquivo.exists())
            {
                return false;
            }

            JsonValue raiz = new JsonReader().parse(arquivo);
            JsonValue perguntasJson = raiz.get("perguntas");
            if (perguntasJson == null || !perguntasJson.isArray())
            {
                return false;
            }

            ArrayList<Pergunta> perguntasCarregadas = new ArrayList<>();
            for (JsonValue perguntaJson : perguntasJson)
            {
                Pergunta pergunta = criarPergunta(perguntaJson);
                if (pergunta != null)
                {
                    perguntasCarregadas.add(pergunta);
                }
            }

            if (perguntasCarregadas.isEmpty())
            {
                return false;
            }

            perguntas = perguntasCarregadas;
            int quantidadeJson = raiz.getInt("quantidadePerguntasPartida", QUANTIDADE_PADRAO_PARTIDA);
            quantidadePerguntasPartida = limitarQuantidadePerguntas(quantidadeJson);
            return true;
        }
        catch (RuntimeException erro)
        {
            return false;
        }
    }

    private Pergunta criarPergunta(JsonValue perguntaJson)
    {
        String enunciado = lerTextoObrigatorio(perguntaJson, "enunciado");
        String alternativaA = lerTextoObrigatorio(perguntaJson, "alternativaA");
        String alternativaB = lerTextoObrigatorio(perguntaJson, "alternativaB");
        String alternativaC = lerTextoObrigatorio(perguntaJson, "alternativaC");
        String alternativaD = lerTextoObrigatorio(perguntaJson, "alternativaD");
        char alternativaCerta = lerAlternativaCerta(perguntaJson);

        if (enunciado == null || alternativaA == null || alternativaB == null
            || alternativaC == null || alternativaD == null || alternativaCerta == '\0')
        {
            return null;
        }

        return new Pergunta(enunciado, alternativaA, alternativaB, alternativaC, alternativaD, alternativaCerta);
    }

    private String lerTextoObrigatorio(JsonValue json, String nomeCampo)
    {
        String valor = json.getString(nomeCampo, null);
        if (valor == null || valor.trim().isEmpty())
        {
            return null;
        }

        return valor;
    }

    private char lerAlternativaCerta(JsonValue json)
    {
        String valor = json.getString("alternativaCerta", null);
        if (valor == null || valor.trim().isEmpty())
        {
            return '\0';
        }

        char alternativa = Character.toLowerCase(valor.trim().charAt(0));
        if (alternativa == 'a' || alternativa == 'b' || alternativa == 'c' || alternativa == 'd')
        {
            return alternativa;
        }

        return '\0';
    }

    private int limitarQuantidadePerguntas(int quantidade)
    {
        if (quantidade <= 0)
        {
            quantidade = QUANTIDADE_PADRAO_PARTIDA;
        }

        return Math.min(quantidade, perguntas.size());
    }

    private void carregarPerguntasPadrao()
    {
        perguntas.clear();
        quantidadePerguntasPartida = QUANTIDADE_PADRAO_PARTIDA;

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

        quantidadePerguntasPartida = limitarQuantidadePerguntas(quantidadePerguntasPartida);
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
        return quantidadePerguntasPartida;
    }
}
