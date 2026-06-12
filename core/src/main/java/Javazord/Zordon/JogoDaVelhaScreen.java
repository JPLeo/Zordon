package Javazord.Zordon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
        raiz.pad(30f);
        raiz.setTouchable(Touchable.childrenOnly);

        Table card = UiEstilo.cardNavy(app.skin);
        card.pad(32f, 40f, 32f, 40f);

        Label titulo = new Label("JOGO DA VELHA", app.skin, "arcade-titulo-card");
        titulo.setAlignment(Align.center);

        lblStatus = new Label("Vez do Jogador: X", app.skin, "arcade-secao");
        lblStatus.setAlignment(Align.center);

        lblPontuacao = new Label("", app.skin, "arcade-corpo-card");
        lblPontuacao.setAlignment(Align.center);

        card.add(titulo).growX().padBottom(10f).row();
        card.add(lblStatus).growX().padBottom(24f).row();

        TextButton.TextButtonStyle estiloPadrao = app.skin.get("arcade-botao", TextButton.TextButtonStyle.class);
        
        TextButton.TextButtonStyle estiloQuadrado = new TextButton.TextButtonStyle();
        estiloQuadrado.font = estiloPadrao.font; // 🛠️ Copia a fonte exata que o projeto usa!
        estiloQuadrado.up = app.skin.newDrawable("white", Color.GOLD); 
        estiloQuadrado.down = app.skin.newDrawable("white", Color.ORANGE); 
        estiloQuadrado.disabled = app.skin.newDrawable("white", new Color(0.85f, 0.7f, 0f, 1f)); 
        estiloQuadrado.fontColor = Color.BLACK;

        Table grade = new Table();
        grade.pad(10f);
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int linha = i;
                final int coluna = j;
                
                btnMatriz[i][j] = new TextButton("-", estiloQuadrado);
                btnMatriz[i][j].getLabel().setFontScale(2.2f); // Deixa o X e O gigantes
                
                btnMatriz[i][j].addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        processarClique(linha, coluna); // 🛠️ Corrigido de column para coluna
                    }
                });
                
                grade.add(btnMatriz[i][j]).width(100f).height(100f).pad(6f);
            }
            grade.row();
        }
        
        card.add(grade).center().padBottom(24f).row();
        card.add(lblPontuacao).growX().padBottom(16f).row();

        btnReiniciar = new TextButton("Jogar Novamente", app.skin, "arcade-botao");
        btnReiniciar.setVisible(false);
        btnReiniciar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                reiniciarPartida();
            }
        });

        TextButton btnSair = new TextButton("Voltar ao Menu", app.skin, "arcade-botao");
        btnSair.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sairDoJogo();
            }
        });

        card.add(btnReiniciar).width(240f).height(46f).padBottom(12f).row();
        card.add(btnSair).width(200f).height(42f).row();

        raiz.add(card).center();
        uiStage.addActor(raiz);
    }

    private void processarClique(int linha, int coluna) {
        String jogadorQueJogou = jogo.getJogadorAtual();
        
        if (jogo.fazerJogada(linha, coluna)) {
            btnMatriz[linha][coluna].setText(jogadorQueJogou);
            btnMatriz[linha][coluna].setDisabled(true);

            if (jogo.isJogoFinalizado()) {
                bloquearTodoOTabuleiro();
                btnReiniciar.setVisible(true);
                
                if (jogo.isTabuleiroCheio() && !lblStatus.getText().toString().contains("Venceu")) {
                    lblStatus.setText("Deu Velha! Empate.");
                } else {
                    lblStatus.setText("Jogador " + jogadorQueJogou + " Venceu!");
                    if (jogadorQueJogou.equals("X")) {
                        pontuacaoGanha = 30;
                        lblPontuacao.setText("+30 Pontos conquistados!");
                        salvarPontuacao();
                    }
                }
            } else {
                lblStatus.setText("Vez do Jogador: " + jogo.getJogadorAtual());
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
        lblStatus.setText("Vez do Jogador: X");
        lblPontuacao.setText("");
        btnReiniciar.setVisible(false);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                btnMatriz[i][j].setText("-");
                btnMatriz[i][j].setDisabled(false);
            }
        }
    }

    private void salvarPontuacao() {
        if (pontuacaoSalva || pontuacaoGanha <= 0 || app.usuarioLogado == null) return;
        pontuacaoSalva = true;

        UsuarioApi.adicionarPontuacao(
            app.usuarioLogado.getIdUsuario(),
            pontuacaoGanha,
            new UsuarioApi.PontuacaoCallback() {
                @Override
                public void sucesso(Usuario usuario) {
                    app.usuarioLogado = usuario;
                    lblPontuacao.setText("Pontuacao salva no banco SQLite!");
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

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        if (uiStage != null) uiStage.dispose();
    }
}