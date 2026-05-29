import java.time.LocalDate;

import Javazord.Zordon.Usuario;

public class Partida
{
    private Usuario usuario;
    private Jogo jogo;
    private int idPartida;
    private int resultado;
    private String modoJogo;
    private int pontuacao;
    private LocalDate dataPartida;

    public Partida(Usuario usuario,Jogo jogo,int idPartida,int resultado,String modoJogo,int pontuacao)
    {
        this.usuario = usuario;
        this.jogo = jogo;
        this.idPartida = idPartida;
        this.resultado = resultado;
        this.modoJogo = modoJogo;
        this.pontuacao = pontuacao;
        this.dataPartida = LocalDate.now();
        usuario.adicionarPontuacao(pontuacao);
    }

    public Usuario getUsuario()
    {
        return usuario;
    }

    public Jogo getJogo()
    {
        return jogo;
    }

    public int getIdPartida()
    {
        return idPartida;
    }

    public int getResultado()
    {
        return resultado;
    }

    public String getModoJogo()
    {
        return modoJogo;
    }

    public int getPontuacao()
    {
        return pontuacao;
    }

    public LocalDate getDataPartida()
    {
        return dataPartida;
    }

}
