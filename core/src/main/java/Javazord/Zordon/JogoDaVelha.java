package Javazord.Zordon;

public class JogoDaVelha {
    private String[][] tabuleiro;
    private String jogadorAtual;
    private boolean jogoFinalizado;

    public JogoDaVelha() {
        tabuleiro = new String[3][3];
        resetarJogo();
    }

    public void resetarJogo() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = "";
            }
        }
        jogadorAtual = "X";
        jogoFinalizado = false;
    }

    public boolean fazerJogada(int linha, int coluna) {
        if (linha < 0 || linha > 2 || coluna < 0 || coluna > 2 || jogoFinalizado) return false;
        
        if (tabuleiro[linha][coluna].equals("")) {
            tabuleiro[linha][coluna] = jogadorAtual;
            if (verificarVencedor(linha, coluna)) {
                jogoFinalizado = true;
            } else if (isTabuleiroCheio()) {
                jogoFinalizado = true;
            } else {
                jogadorAtual = jogadorAtual.equals("X") ? "O" : "X";
            }
            return true;
        }
        return false;
    }

    private boolean verificarVencedor(int l, int c) {
        if (tabuleiro[l][0].equals(jogadorAtual) && tabuleiro[l][1].equals(jogadorAtual) && tabuleiro[l][2].equals(jogadorAtual)) return true;
        if (tabuleiro[0][c].equals(jogadorAtual) && tabuleiro[1][c].equals(jogadorAtual) && tabuleiro[2][c].equals(jogadorAtual)) return true;
        if (tabuleiro[0][0].equals(jogadorAtual) && tabuleiro[1][1].equals(jogadorAtual) && tabuleiro[2][2].equals(jogadorAtual)) return true;
        if (tabuleiro[0][2].equals(jogadorAtual) && tabuleiro[1][1].equals(jogadorAtual) && tabuleiro[2][0].equals(jogadorAtual)) return true;
        return false;
    }

    public boolean isTabuleiroCheio() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j].equals("")) return false;
            }
        }
        return true;
    }

    public String[][] getTabuleiro() { return tabuleiro; }
    public String getJogadorAtual() { return jogadorAtual; }
    public boolean isJogoFinalizado() { return jogoFinalizado; }
}