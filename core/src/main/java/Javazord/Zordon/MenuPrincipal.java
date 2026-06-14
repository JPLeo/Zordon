package Javazord.Zordon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuPrincipal implements Screen {
    private static final float LARGURA_MINIMA_GRID = 780f;
    private static final float ALTURA_CARD_JOGO = 190f;
    private static final float LARGURA_MAXIMA_GRID = 860f;
    private static final float LARGURA_CARD_STAT = 220f;

    final AppEntrada app;
    private Stage stage;
    private Table conteudoScroll;
    private Table gridJogos;
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
        conteudoScroll.pad(30f, 48f, 40f, 48f);
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
            conteudoScroll.setWidth(Math.max(320f, larguraTela - 96f));
        }
        montarGridJogos();
    }

    private void montarCabecalho(Table conteudo) {
        Table cabecalho = new Table();
        cabecalho.add(UiEstilo.marcaApp(app.skin, "Zordon")).left().expandX();
        conteudo.add(cabecalho).growX().padBottom(22f).row();
    }

    private void montarBoasVindas(Table conteudo) {
        Table card = UiEstilo.cardNavy(app.skin);
        card.pad(18f, 22f, 18f, 22f);

        String primeiroNome = app.usuarioLogado.getNome().split(" ")[0];
        Label saudacao = new Label("Olá, " + primeiroNome + "!", app.skin, "arcade-titulo-card");
        saudacao.setAlignment(Align.left);

        Label mensagem = new Label(
            "Que bom ver você de volta. Pronto para exercitar a mente hoje?",
            app.skin,
            "arcade-corpo-card"
        );
        mensagem.setWrap(true);
        mensagem.setAlignment(Align.left);

        card.add(saudacao).left().growX().row();
        card.add(mensagem).left().growX().padTop(10f).row();
        conteudo.add(card).growX().height(112f).padBottom(18f).row();
    }

    private void montarEstatisticas(Table conteudo) {
        Table linha = new Table();
        linha.defaults().width(LARGURA_CARD_STAT).height(86f).padRight(12f);

        Table saldo = UiEstilo.cardEstatistica(
            app.skin,
            "Pontuação total",
            app.usuarioLogado.getPontuacaoTotal() + " pts"
        );
        Table jogos = UiEstilo.cardEstatistica(app.skin, "Jogos disponíveis", "3");

        linha.add(saldo);
        linha.add(jogos).padRight(0f);
        conteudo.add(linha).width(LARGURA_CARD_STAT * 2f + 12f).left().padBottom(22f).row();
    }

    private void montarBiblioteca(Table conteudo) {
        Label tituloSecao = new Label("Biblioteca de Jogos", app.skin, "arcade-secao");
        tituloSecao.setAlignment(Align.left);
        conteudo.add(tituloSecao).left().padBottom(14f).row();

        gridJogos = new Table();
        conteudo.add(gridJogos).growX().maxWidth(LARGURA_MAXIMA_GRID).left().row();
        montarGridJogos();
    }

    private void montarGridJogos() {
        if (gridJogos == null || app.usuarioLogado == null) {
            return;
        }

        gridJogos.clearChildren();
        gridJogos.defaults().width(duasColunas() ? 424f : Math.min(520f, conteudoScroll.getWidth()))
            .height(ALTURA_CARD_JOGO).padBottom(16f);

        boolean duasColunas = duasColunas();

        adicionarCardJogo(gridJogos, duasColunas, 0,
            app.skin,
            "Agilidade",
            "Balde das Gotas",
            "Mova o balde e colete o máximo de gotas antes que elas caiam.",
            "Jogar Balde",
            () -> {
                app.setScreen(new DropGameScreen(app));
                dispose();
            }
        );

        adicionarCardJogo(gridJogos, duasColunas, 1,
            app.skin,
            "Conhecimento",
            "Quiz",
            "Responda perguntas de conhecimentos gerais e some pontos.",
            "Jogar Quiz",
            () -> {
                app.setScreen(new QuizGameScreen(app));
                dispose();
            }
        );

        adicionarCardJogo(gridJogos, duasColunas, 2,
            app.skin,
            "Estratégia",
            "Jogo da Velha",
            "Desafie a lógica em um tabuleiro clássico 3x3 e conquiste pontos.",
            "Jogar Velha",
            () -> {
                app.setScreen(new JogoDaVelhaScreen(app));
                dispose();
            }
        );
    }

    private void adicionarCardJogo(Table grid, boolean duasColunas, int indice, Skin skin,
                                  String categoria, String titulo, String descricao,
                                  String textoBotao, Runnable aoJogar) {
        Table card = UiEstilo.cardJogo(skin, categoria, titulo, descricao, textoBotao, aoJogar);

        if (duasColunas) {
            grid.add(card).padRight(indice % 2 == 0 ? 16f : 0f);
            if (indice % 2 == 1) {
                grid.row();
            }
        } else {
            grid.add(card).row();
        }
    }

    private boolean duasColunas() {
        return conteudoScroll != null && conteudoScroll.getWidth() >= LARGURA_MINIMA_GRID;
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
