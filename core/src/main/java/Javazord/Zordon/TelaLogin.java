package Javazord.Zordon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class TelaLogin implements Screen {
    private static final float LARGURA_CARD = 500f;
    private static final float LARGURA_CONTEUDO = 420f;

    private final AppEntrada app;
    private Stage stage;
    private TextField campoNome;
    private TextButton btnEntrar;
    private Label lblErro;
    private Label lblStatus;
    private boolean enviando;

    public TelaLogin(final AppEntrada app) {
        this.app = app;
        stage = new Stage(app.sViewport);
        montarLayout();
    }

    private void montarLayout() {
        Table raiz = new Table();
        raiz.setFillParent(true);
        raiz.pad(42f, 56f, 42f, 56f);

        Table card = UiEstilo.cardNavy(app.skin);
        card.pad(28f, 34f, 30f, 34f);

        Label saudacao = new Label("Bem-vindo!", app.skin, "arcade-titulo-card");
        saudacao.setAlignment(Align.left);

        Label instrucao = new Label(
            "Digite seu nome para começar.\nSeu progresso será salvo automaticamente.",
            app.skin,
            "arcade-corpo-card"
        );
        instrucao.setWrap(true);
        instrucao.setAlignment(Align.left);

        Label lblNome = new Label("Nome do jogador", app.skin, "arcade-corpo-card");

        campoNome = new TextField("", app.skin, "arcade-campo-texto");
        campoNome.setMessageText("Ex.: João Silva");

        btnEntrar = new TextButton("Jogar agora", app.skin, "arcade-botao");

        lblErro = new Label("", app.skin, "arcade-erro");
        lblErro.setAlignment(Align.center);

        lblStatus = new Label("", app.skin, "arcade-status");
        lblStatus.setAlignment(Align.center);

        card.add(saudacao).left().row();
        card.add(instrucao).width(LARGURA_CONTEUDO).left().padTop(10f).row();
        card.add(lblNome).left().padTop(22f).row();
        card.add(campoNome).width(LARGURA_CONTEUDO).height(46f).padTop(8f).row();
        card.add(btnEntrar).width(180f).height(46f).left().padTop(18f).row();

        raiz.add(UiEstilo.marcaApp(app.skin, "Zordon")).center().row();
        raiz.add(UiEstilo.subtituloApp(app.skin, "Plataforma de Jogos")).padTop(6f).center().row();
        raiz.add().height(34f).row();
        raiz.add(card).width(LARGURA_CARD).maxWidth(LARGURA_CARD).growX().center().row();
        raiz.add(lblErro).width(LARGURA_CARD).minHeight(22f).padTop(14f).center().row();
        raiz.add(lblStatus).width(LARGURA_CARD).minHeight(22f).padTop(6f).center().row();

        btnEntrar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmarEntrada();
            }
        });

        stage.addActor(raiz);
    }

    private void confirmarEntrada() {
        if (enviando) {
            return;
        }

        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            lblErro.setText("Informe seu nome para continuar.");
            return;
        }

        enviando = true;
        lblErro.setText("");
        lblStatus.setText("Salvando jogador...");
        btnEntrar.setDisabled(true);
        campoNome.setDisabled(true);

        UsuarioApi.entrar(nome, new UsuarioApi.EntrarCallback() {
            @Override
            public void sucesso(Usuario usuario) {
                app.usuarioLogado = usuario;
                app.setScreen(new MenuPrincipal(app));
                dispose();
            }

            @Override
            public void erro(String mensagem) {
                enviando = false;
                lblStatus.setText("");
                lblErro.setText(mensagem);
                btnEntrar.setDisabled(false);
                campoNome.setDisabled(false);
            }
        });
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            confirmarEntrada();
        }

        ScreenUtils.clear(UiEstilo.FUNDO);
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(campoNome);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
