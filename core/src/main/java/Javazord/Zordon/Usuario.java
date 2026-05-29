package Javazord.Zordon;

public class Usuario
{
    private int idUsuario;
    private String nome;
    private int pontuacaoTotal;

    public Usuario(int idUsuario,String nome)
    {
        this(idUsuario, nome, 0);
    }

    public Usuario(int idUsuario, String nome, int pontuacaoTotal)
    {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public int getIdUsuario()
    {
        return idUsuario;
    }
    public String getNome()
    {
        return nome;
    }
    public int getPontuacaoTotal()
    {
        return pontuacaoTotal;
    }
    public void adicionarPontuacao(int pontos)
    {
        pontuacaoTotal += pontos;
    }
}
