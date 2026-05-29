/**
 * João Pedro Dias - 2025.2.0120.0007-2
 * 2026.1-CMP1223/C01 - Fundamentos de Programação
 * orientada a objetos
 */
package Javazord.Zordon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuPrincipal implements Screen {
    final AppEntrada app;
    private Stage stage;
    private Table conteudoScroll;
    private ScrollPane scrollPane;
    private InputAdapter scrollMouse;

    public MenuPrincipal(final AppEntrada app) {
        this.app = app;
        stage = new Stage(app.sViewport);
        montarLayout();
    }

    private void montarLayout() {
        conteudoScroll = new Table();
        conteudoScroll.top().left();
        conteudoScroll.pad(40f, 56f, 48f, 56f);
        conteudoScroll.defaults().growX();

        if (app.usuarioLogado != null) {
            montarCabecalho(conteudoScroll);
            montarBoasVindas(conteudoScroll);
            montarEstatisticas(conteudoScroll);
            montarBiblioteca(conteudoScroll);
        }

        scrollPane = UiEstilo.scrollPagina(app.skin, conteudoScroll);

        Table raiz = new Table();
        raiz.setFillParent(true);
        raiz.add(scrollPane).grow();

        stage.addActor(raiz);
        atualizarLarguraConteudo(Gdx.graphics.getWidth());

        scrollMouse = new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (scrollPane == null) {
                    return false;
                }
                scrollPane.setScrollY(MathUtils.clamp(
                    scrollPane.getScrollY() - amountY * 80f,
                    0f,
                    scrollPane.getMaxY()
                ));
                return true;
            }
        };
    }

    private void atualizarLarguraConteudo(int larguraTela) {
        if (conteudoScroll != null) {
            conteudoScroll.setWidth(Math.max(320f, larguraTela - 112f));
        }
    }

    private void montarCabecalho(Table conteudo) {
        Table cabecalho = new Table();
        cabecalho.add(UiEstilo.tituloApp(app.skin, "Zordon")).left().expandX();
        cabecalho.add(UiEstilo.badge(app.skin, app.usuarioLogado.getPontuacaoTotal() + " pts")).right();
        conteudo.add(cabecalho).growX().padBottom(28f).row();
    }

    private void montarBoasVindas(Table conteudo) {
        Table card = UiEstilo.cardNavy(app.skin);
        card.pad(28f, 32f, 28f, 32f);

        String primeiroNome = app.usuarioLogado.getNome().split(" ")[0];
        Label saudacao = new Label("Ola, " + primeiroNome + "!", app.skin, "arcade-titulo-card");
        saudacao.setAlignment(Align.left);

        Label mensagem = new Label(
            "Que bom ver voce de volta. Pronto para exercitar a mente hoje?",
            app.skin,
            "arcade-corpo-card"
        );
        mensagem.setWrap(true);
        mensagem.setAlignment(Align.left);

        card.add(saudacao).left().growX().row();
        card.add(mensagem).left().growX().padTop(10f).row();
        conteudo.add(card).growX().padBottom(24f).row();
    }

    private void montarEstatisticas(Table conteudo) {
        Table linha = new Table();
        linha.defaults().expandX().fillX().padRight(12f);

        Table saldo = UiEstilo.cardEstatistica(
            app.skin,
            "Saldo atual",
            app.usuarioLogado.getPontuacaoTotal() + " pts"
        );
        Table sequencia = UiEstilo.cardEstatistica(app.skin, "Sequencia", "1 dia");

        linha.add(saldo);
        linha.add(sequencia).padRight(0f);
        conteudo.add(linha).growX().padBottom(28f).row();
    }

    private void montarBiblioteca(Table conteudo) {
        Label tituloSecao = new Label("Biblioteca de Jogos", app.skin, "arcade-secao");
        tituloSecao.setAlignment(Align.left);
        conteudo.add(tituloSecao).left().padBottom(20f).row();

        conteudo.add(UiEstilo.cardJogo(
            app.skin,
            "Agilidade",
            "Balde das Gotas",
            "Mova o balde e colete o maximo de gotas antes que elas caiam.",
            () -> {
                app.setScreen(new DropGameScreen(app));
                dispose();
            }
        )).growX().minHeight(220f).padBottom(16f).row();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(UiEstilo.FUNDO);
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        atualizarLarguraConteudo(width);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, scrollMouse));
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
