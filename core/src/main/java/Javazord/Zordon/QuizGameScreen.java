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

public class QuizGameScreen implements Screen
{
    private static final float LARGURA_CARD = 690f;
    private static final float LARGURA_PERGUNTA = 610f;
    private static final float LARGURA_BOTAO = 520f;

    final AppEntrada app;

    private Stage uiStage;
    private Label lblProgresso;
    private Label lblPontuacao;
    private Label lblPergunta;
    private Label lblResultado;
    private int pontuacao;
    private int indicePerguntaAtual;
    private boolean pontuacaoSalva;

    private TextButton btnA;
    private TextButton btnB;
    private TextButton btnC;
    private TextButton btnD;
    private TextButton btnProxima;

    private Quiz quiz;
    private Pergunta perguntaAtual;

    public QuizGameScreen(final AppEntrada appParam)
    {
        this.app = appParam;
        this.pontuacao = 0;
        this.pontuacaoSalva = false;

        quiz = new Quiz();
        quiz.sortearPerguntas();
        indicePerguntaAtual = 0;
        perguntaAtual = quiz.getPergunta(indicePerguntaAtual);

        montarTelaQuiz();
    }

    private void montarTelaQuiz()
    {
        uiStage = new Stage(app.sViewport);

        Table raiz = new Table();
        raiz.setFillParent(true);
        raiz.pad(26f);
        raiz.setTouchable(Touchable.childrenOnly);

        Table card = UiEstilo.cardNavy(app.skin);
        card.pad(24f, 30f, 24f, 30f);

        Label titulo = new Label("Quiz", app.skin, "arcade-titulo-card");
        titulo.setAlignment(Align.center);

        lblProgresso = new Label("", app.skin, "arcade-corpo-card");
        lblProgresso.setAlignment(Align.center);

        lblPontuacao = new Label("Pontuação: " + pontuacao, app.skin, "arcade-corpo-card");
        lblPontuacao.setAlignment(Align.center);

        lblPergunta = new Label(perguntaAtual.getEnunciado(), app.skin, "arcade-corpo-card");
        lblPergunta.setWrap(true);
        lblPergunta.setAlignment(Align.center);

        lblResultado = new Label("", app.skin, "arcade-corpo-card");
        lblResultado.setAlignment(Align.center);

        btnA = criarBotaoResposta(perguntaAtual.getAlternativaA());
        btnB = criarBotaoResposta(perguntaAtual.getAlternativaB());
        btnC = criarBotaoResposta(perguntaAtual.getAlternativaC());
        btnD = criarBotaoResposta(perguntaAtual.getAlternativaD());

        btnA.addListener(criarListenerResposta('a'));
        btnB.addListener(criarListenerResposta('b'));
        btnC.addListener(criarListenerResposta('c'));
        btnD.addListener(criarListenerResposta('d'));

        btnProxima = new TextButton("Próxima", app.skin, "arcade-botao");
        btnProxima.setVisible(false);
        btnProxima.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                proximaPergunta();
            }
        });

        TextButton btnSair = new TextButton("Sair", app.skin, "arcade-botao-navy");
        btnSair.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                sairDoQuiz();
            }
        });

        atualizarProgresso();
        card.add(titulo).growX().padBottom(12f).row();
        card.add(lblProgresso).growX().padBottom(6f).row();
        card.add(lblPontuacao).growX().padBottom(16f).row();
        card.add(lblPergunta).width(LARGURA_PERGUNTA).growX().padBottom(16f).row();

        card.add(btnA).width(LARGURA_BOTAO).growX().minHeight(44f).padBottom(8f).row();
        card.add(btnB).width(LARGURA_BOTAO).growX().minHeight(44f).padBottom(8f).row();
        card.add(btnC).width(LARGURA_BOTAO).growX().minHeight(44f).padBottom(8f).row();
        card.add(btnD).width(LARGURA_BOTAO).growX().minHeight(44f).padBottom(16f).row();

        card.add(lblResultado).growX().minHeight(24f).padBottom(12f).row();
        card.add(btnProxima).width(220f).height(42f).padBottom(12f).row();
        card.add(btnSair).width(140f).height(40f).row();

        raiz.add(card).width(LARGURA_CARD).maxWidth(LARGURA_CARD).growX().center();
        uiStage.addActor(raiz);
    }

    private TextButton criarBotaoResposta(String texto)
    {
        TextButton botao = new TextButton(texto, app.skin, "arcade-botao-resposta");
        botao.getLabel().setWrap(true);
        botao.getLabel().setAlignment(Align.center);
        return botao;
    }

    private ChangeListener criarListenerResposta(final char alternativaEscolhida)
    {
        return new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if (perguntaAtual.conferePergunta(alternativaEscolhida))
                {
                    pontuacao += 10;
                    lblPontuacao.setText("Pontuação: " + pontuacao);
                    lblResultado.setText("Resposta correta! +10 pts");
                }
                else
                {
                    lblResultado.setText("Resposta incorreta! Correta: " + getTextoAlternativaCorreta());
                }

                mostrarFeedback(alternativaEscolhida);
                bloquearAlternativas();
                btnProxima.setVisible(true);
            }
        };
    }

    private void mostrarFeedback(char alternativaEscolhida)
    {
        char correta = perguntaAtual.getAlternativaCerta();
        TextButton botaoCorreto = getBotaoAlternativa(correta);
        TextButton botaoEscolhido = getBotaoAlternativa(alternativaEscolhida);

        botaoCorreto.setStyle(app.skin.get("arcade-botao-correto", TextButton.TextButtonStyle.class));
        if (alternativaEscolhida != correta)
        {
            botaoEscolhido.setStyle(app.skin.get("arcade-botao-incorreto", TextButton.TextButtonStyle.class));
        }
    }

    private TextButton getBotaoAlternativa(char alternativa)
    {
        if (alternativa == 'a')
        {
            return btnA;
        }
        else if (alternativa == 'b')
        {
            return btnB;
        }
        else if (alternativa == 'c')
        {
            return btnC;
        }
        else
        {
            return btnD;
        }
    }

    private void bloquearAlternativas()
    {
        btnA.setDisabled(true);
        btnB.setDisabled(true);
        btnC.setDisabled(true);
        btnD.setDisabled(true);
    }

    private void liberarAlternativas()
    {
        TextButton.TextButtonStyle estiloResposta = app.skin.get("arcade-botao-resposta", TextButton.TextButtonStyle.class);
        btnA.setStyle(estiloResposta);
        btnB.setStyle(estiloResposta);
        btnC.setStyle(estiloResposta);
        btnD.setStyle(estiloResposta);

        btnA.setDisabled(false);
        btnB.setDisabled(false);
        btnC.setDisabled(false);
        btnD.setDisabled(false);
    }

    private void proximaPergunta()
    {
        indicePerguntaAtual++;

        if (indicePerguntaAtual >= quiz.getQuantidadePerguntasPartida())
        {
            finalizarQuiz();
            return;
        }

        perguntaAtual = quiz.getPergunta(indicePerguntaAtual);

        atualizarProgresso();
        lblPergunta.setText(perguntaAtual.getEnunciado());
        lblResultado.setText("");

        btnA.setText(perguntaAtual.getAlternativaA());
        btnB.setText(perguntaAtual.getAlternativaB());
        btnC.setText(perguntaAtual.getAlternativaC());
        btnD.setText(perguntaAtual.getAlternativaD());

        liberarAlternativas();
        btnProxima.setVisible(false);
    }

    private void finalizarQuiz()
    {
        lblProgresso.setText("Quiz finalizado");
        lblPergunta.setText("Fim do Quiz! Pontuação final: " + pontuacao);
        lblResultado.setText("Salvando pontuação...");

        btnA.setVisible(false);
        btnB.setVisible(false);
        btnC.setVisible(false);
        btnD.setVisible(false);
        btnProxima.setVisible(false);

        salvarPontuacao();
    }

    private void salvarPontuacao()
    {
        if (pontuacaoSalva)
        {
            return;
        }

        pontuacaoSalva = true;

        if (pontuacao <= 0)
        {
            lblResultado.setText("Nenhum ponto para salvar.");
            return;
        }

        if (app.usuarioLogado == null)
        {
            lblResultado.setText("Usuário não encontrado.");
            return;
        }

        UsuarioApi.adicionarPontuacao(
            app.usuarioLogado.getIdUsuario(),
            pontuacao,
            new UsuarioApi.PontuacaoCallback()
            {
                @Override
                public void sucesso(Usuario usuario)
                {
                    app.usuarioLogado = usuario;
                    lblResultado.setText("Pontuação salva!");
                }

                @Override
                public void erro(String mensagem)
                {
                    lblResultado.setText(mensagem);
                }
            }
        );
    }

    private String getTextoAlternativaCorreta()
    {
        char correta = perguntaAtual.getAlternativaCerta();

        if (correta == 'a')
        {
            return perguntaAtual.getAlternativaA();
        }
        else if (correta == 'b')
        {
            return perguntaAtual.getAlternativaB();
        }
        else if (correta == 'c')
        {
            return perguntaAtual.getAlternativaC();
        }
        else
        {
            return perguntaAtual.getAlternativaD();
        }
    }

    private void atualizarProgresso()
    {
        lblProgresso.setText("Pergunta " + (indicePerguntaAtual + 1) + "/" + quiz.getQuantidadePerguntasPartida());
    }

    private void sairDoQuiz()
    {
        app.setScreen(new MenuPrincipal(app));
        dispose();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new InputMultiplexer(uiStage));
    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            sairDoQuiz();
        }

        ScreenUtils.clear(UiEstilo.FUNDO);
        uiStage.getViewport().apply();
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose()
    {
        if (uiStage != null)
        {
            uiStage.dispose();
        }
    }
}
