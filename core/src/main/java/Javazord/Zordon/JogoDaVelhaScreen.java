package Javazord.Zordon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class JogoDaVelhaScreen implements Screen {
    private static final float LARGURA_CARD = 520f;
    private static final float TAMANHO_CELULA = 72f;

    final AppEntrada app;

    private Stage uiStage;
    private Label lblStatus;
    private Label lblPontuacao;
    private int pontuacaoGanha;
    private boolean pontuacaoSalva;

    private TextButton[][] btnMatriz;
    private TextButton btnReiniciar;
    private JogoDaVelha jogo;

    public JogoDaVelhaScreen(final AppEntrada appParam) {
        this.app = appParam;
        this.pontuacaoGanha = 0;
        this.pontuacaoSalva = false;
        this.jogo = new JogoDaVelha();
        this.btnMatriz = new TextButton[3][3];

        montarTela();
    }

    private void montarTela() {
        uiStage = new Stage(app.sViewport);

        Table raiz = new Table();
        raiz.setFillParent(true);
        raiz.pad(26f);
        raiz.setTouchable(Touchable.childrenOnly);

        Table card = UiEstilo.cardNavy(app.skin);
        card.pad(24f, 30f, 24f, 30f);

        Label titulo = new Label("Jogo da Velha", app.skin, "arcade-titulo-card");
        titulo.setAlignment(Align.center);

        lblStatus = new Label("Vez do jogador: X", app.skin, "arcade-corpo-card");
        lblStatus.setAlignment(Align.center);

        lblPontuacao = new Label("", app.skin, "arcade-corpo-card");
        lblPontuacao.setAlignment(Align.center);

        card.add(titulo).growX().padBottom(8f).row();
        card.add(lblStatus).growX().padBottom(18f).row();
        card.add(criarGrade()).center().padBottom(18f).row();
        card.add(lblPontuacao).growX().minHeight(24f).padBottom(12f).row();

        btnReiniciar = new TextButton("Jogar novamente", app.skin, "arcade-botao");
        btnReiniciar.setVisible(false);
        btnReiniciar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                reiniciarPartida();
            }
        });

        TextButton btnSair = new TextButton("Voltar ao Menu", app.skin, "arcade-botao-navy");
        btnSair.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sairDoJogo();
            }
        });

        card.add(btnReiniciar).width(190f).height(42f).padBottom(10f).row();
        card.add(btnSair).width(170f).height(40f).row();

        raiz.add(card).width(LARGURA_CARD).maxWidth(LARGURA_CARD).growX().center();
        uiStage.addActor(raiz);
    }

    private Table criarGrade() {
        Table grade = new Table();
        grade.pad(4f);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int linha = i;
                final int coluna = j;

                TextButton celula = new TextButton("", app.skin, "arcade-celula-tabuleiro");
                celula.getLabel().setAlignment(Align.center);
                celula.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        processarClique(linha, coluna);
                    }
                });

                btnMatriz[i][j] = celula;
                grade.add(celula).width(TAMANHO_CELULA).height(TAMANHO_CELULA).pad(5f);
            }
            grade.row();
        }

        return grade;
    }

    private void processarClique(int linha, int coluna) {
        String jogadorQueJogou = jogo.getJogadorAtual();

        if (jogo.fazerJogada(linha, coluna)) {
            TextButton celula = btnMatriz[linha][coluna];
            celula.setText(jogadorQueJogou);
            celula.setStyle(app.skin.get(
                jogadorQueJogou.equals("X") ? "arcade-celula-x" : "arcade-celula-o",
                TextButton.TextButtonStyle.class
            ));
            celula.setDisabled(true);

            if (jogo.isJogoFinalizado()) {
                bloquearTodoOTabuleiro();
                btnReiniciar.setVisible(true);

                if (jogo.isTabuleiroCheio() && !lblStatus.getText().toString().contains("venceu")) {
                    lblStatus.setText("Deu velha! Empate.");
                } else {
                    lblStatus.setText("Jogador " + jogadorQueJogou + " venceu!");
                    if (jogadorQueJogou.equals("X")) {
                        pontuacaoGanha = 30;
                        lblPontuacao.setText("+30 pontos conquistados!");
                        salvarPontuacao();
                    }
                }
            } else {
                lblStatus.setText("Vez do jogador: " + jogo.getJogadorAtual());
            }
        }
    }

    private void bloquearTodoOTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                btnMatriz[i][j].setDisabled(true);
            }
        }
    }

    private void reiniciarPartida() {
        jogo.resetarJogo();
        pontuacaoGanha = 0;
        pontuacaoSalva = false;
        lblStatus.setText("Vez do jogador: X");
        lblPontuacao.setText("");
        btnReiniciar.setVisible(false);

        TextButton.TextButtonStyle estiloCelula = app.skin.get(
            "arcade-celula-tabuleiro",
            TextButton.TextButtonStyle.class
        );

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                btnMatriz[i][j].setText("");
                btnMatriz[i][j].setStyle(estiloCelula);
                btnMatriz[i][j].setDisabled(false);
            }
        }
    }

    private void salvarPontuacao() {
        if (pontuacaoSalva || pontuacaoGanha <= 0 || app.usuarioLogado == null) {
            return;
        }

        pontuacaoSalva = true;

        UsuarioApi.adicionarPontuacao(
            app.usuarioLogado.getIdUsuario(),
            pontuacaoGanha,
            new UsuarioApi.PontuacaoCallback() {
                @Override
                public void sucesso(Usuario usuario) {
                    app.usuarioLogado = usuario;
                    lblPontuacao.setText("Pontuação salva!");
                }

                @Override
                public void erro(String mensagem) {
                    lblPontuacao.setText("Erro ao salvar: " + mensagem);
                }
            }
        );
    }

    private void sairDoJogo() {
        app.setScreen(new MenuPrincipal(app));
        dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(uiStage));
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sairDoJogo();
        }

        ScreenUtils.clear(UiEstilo.FUNDO);
        uiStage.getViewport().apply();
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        if (uiStage != null) {
            uiStage.dispose();
        }
    }
}
