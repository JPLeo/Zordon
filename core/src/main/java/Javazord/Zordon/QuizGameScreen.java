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
    final AppEntrada app;

    private Stage uiStage;
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
        raiz.pad(30f);
        raiz.setTouchable(Touchable.childrenOnly);

        Table card = UiEstilo.cardNavy(app.skin);
        card.pad(28f, 32f, 28f, 32f);

        Label titulo = new Label("Quiz", app.skin, "arcade-titulo-card");
        titulo.setAlignment(Align.center);

        lblPontuacao = new Label("Pontuacao: " + pontuacao, app.skin, "arcade-corpo-card");
        lblPontuacao.setAlignment(Align.center);

        lblPergunta = new Label(perguntaAtual.getEnunciado(), app.skin, "arcade-corpo-card");
        lblPergunta.setWrap(true);
        lblPergunta.setAlignment(Align.center);

        lblResultado = new Label("", app.skin, "arcade-corpo-card");
        lblResultado.setAlignment(Align.center);

        btnA = new TextButton(perguntaAtual.getAlternativaA(), app.skin, "arcade-botao");
        btnB = new TextButton(perguntaAtual.getAlternativaB(), app.skin, "arcade-botao");
        btnC = new TextButton(perguntaAtual.getAlternativaC(), app.skin, "arcade-botao");
        btnD = new TextButton(perguntaAtual.getAlternativaD(), app.skin, "arcade-botao");

        btnA.addListener(criarListenerResposta('a'));
        btnB.addListener(criarListenerResposta('b'));
        btnC.addListener(criarListenerResposta('c'));
        btnD.addListener(criarListenerResposta('d'));

        btnProxima = new TextButton("Proxima", app.skin, "arcade-botao");
        btnProxima.setVisible(false);
        btnProxima.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                proximaPergunta();
            }
        });

        TextButton btnSair = new TextButton("Sair", app.skin, "arcade-botao");
        btnSair.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                sairDoQuiz();
            }
        });

        card.add(titulo).growX().padBottom(18f).row();
        card.add(lblPontuacao).growX().padBottom(18f).row();
        card.add(lblPergunta).width(700f).padBottom(18f).row();

        card.add(btnA).width(520f).height(48f).padBottom(10f).row();
        card.add(btnB).width(520f).height(48f).padBottom(10f).row();
        card.add(btnC).width(520f).height(48f).padBottom(10f).row();
        card.add(btnD).width(520f).height(48f).padBottom(18f).row();

        card.add(lblResultado).growX().padBottom(14f).row();
        card.add(btnProxima).width(220f).height(42f).padBottom(12f).row();
        card.add(btnSair).width(180f).height(42f).row();

        raiz.add(card).center();
        uiStage.addActor(raiz);
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
                    lblPontuacao.setText("Pontuacao: " + pontuacao);
                    lblResultado.setText("Resposta correta!");
                }
                else
                {
                    lblResultado.setText("Resposta incorreta! Correta: " + getTextoAlternativaCorreta());
                }

                bloquearAlternativas();
                btnProxima.setVisible(true);
            }
        };
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
        lblPergunta.setText("Fim do Quiz! Pontuacao final: " + pontuacao);
        lblResultado.setText("Salvando pontuacao...");

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
            lblResultado.setText("Usuario nao encontrado.");
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
                    lblResultado.setText("Pontuacao salva!");
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