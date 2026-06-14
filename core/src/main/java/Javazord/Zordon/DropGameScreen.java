package Javazord.Zordon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class DropGameScreen implements Screen {
    private static final float VELOCIDADE_INICIAL_GOTA = 2f;
    private static final float INCREMENTO_VELOCIDADE_GOTA = 0.08f;
    private static final float VELOCIDADE_MAXIMA_GOTA = 5f;

    final AppEntrada app;

    float gotaTempo;
    float tempoPartida;
    Vector2 touchPos;
    int gotasColetadas;
    boolean partidaEncerrada;
    boolean saindoDoJogo;

    Rectangle baldeRet;
    Rectangle gotaRet;

    Texture fundoTx;
    Texture baldeTx;
    Sprite baldeSprite;
    Texture gotaTx;
    Array<Sprite> gotaSprites;

    Sound gotaSom;
    Music musica;

    private Stage uiStage;
    private Label lblPontuacao;
    private InputAdapter controleJogo;

    public DropGameScreen(final AppEntrada appParam) {
        this.app = appParam;

        touchPos = new Vector2();

        fundoTx = new Texture("DropGame/background.png");
        baldeTx = new Texture("DropGame/bucket.png");
        baldeSprite = new Sprite(baldeTx);
        baldeSprite.setSize(1, 1);
        gotaTx = new Texture("DropGame/drop.png");
        gotaSprites = new Array<>();

        baldeRet = new Rectangle();
        gotaRet = new Rectangle();

        gotaSom = Gdx.audio.newSound(Gdx.files.internal("DropGame/drop.mp3"));
        musica = Gdx.audio.newMusic(Gdx.files.internal("DropGame/music.mp3"));
        musica.setLooping(true);
        musica.setVolume(.5f);

        montarHud();
        montarControles();
    }

    private void montarHud() {
        uiStage = new Stage(app.sViewport);

        Table hud = new Table();
        hud.setFillParent(true);
        hud.top();
        hud.pad(10f, 18f, 0f, 18f);
        hud.setTouchable(Touchable.childrenOnly);

        Table barra = new Table(app.skin);
        barra.setBackground(app.skin.getDrawable("arcade-pill-navy"));
        barra.pad(6f, 14f, 6f, 14f);

        lblPontuacao = new Label("Gotas coletadas: 0", app.skin, "arcade-hud-texto");

        TextButton btnSair = new TextButton("Sair", app.skin, "arcade-botao-incorreto");
        btnSair.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sairDoJogo();
            }
        });

        barra.add(lblPontuacao).left().expandX().padRight(12f);
        barra.add(btnSair).width(86f).height(34f).right();

        hud.add(barra).growX().height(46f).row();
        uiStage.addActor(hud);
    }

    private void montarControles() {
        controleJogo = new InputAdapter() {
            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                if (partidaEncerrada) {
                    return false;
                }
                moverBalde(x, y);
                return true;
            }

            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (partidaEncerrada) {
                    return false;
                }
                moverBalde(x, y);
                return true;
            }
        };
    }

    private void moverBalde(int x, int y) {
        if (partidaEncerrada) {
            return;
        }
        touchPos.set(x, y);
        app.fViewport.unproject(touchPos);
        baldeSprite.setCenterX(touchPos.x);
    }

    private void sairDoJogo() {
        if (saindoDoJogo) {
            return;
        }
        saindoDoJogo = true;
        musica.stop();
        if (gotasColetadas > 0 && app.usuarioLogado != null) {
            UsuarioApi.adicionarPontuacao(app.usuarioLogado.getIdUsuario(), gotasColetadas, new UsuarioApi.PontuacaoCallback() {
                @Override
                public void sucesso(Usuario usuario) {
                    app.usuarioLogado = usuario;
                    app.setScreen(new MenuPrincipal(app));
                    dispose();
                }

                @Override
                public void erro(String mensagem) {
                    app.setScreen(new MenuPrincipal(app));
                    dispose();
                }
            });
        } else {
            app.setScreen(new MenuPrincipal(app));
            dispose();
        }
    }

    @Override
    public void show() {
        musica.play();
        Gdx.input.setInputProcessor(new InputMultiplexer(uiStage, controleJogo));
    }

    @Override
    public void render(float delta) {
        inputTeclado();
        logic(delta);
        draw();
        drawHud(delta);
    }

    private void inputTeclado() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sairDoJogo();
            return;
        }

        if (partidaEncerrada) {
            return;
        }

        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            baldeSprite.translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            baldeSprite.translateX(-speed * delta);
        }
    }

    private void logic(float delta) {
        if (partidaEncerrada) {
            return;
        }

        tempoPartida += delta;
        float cenaLarg = app.fViewport.getWorldWidth();
        float baldeLarg = baldeSprite.getWidth();
        float baldeAlt = baldeSprite.getHeight();
        float velocidadeGota = Math.min(
            VELOCIDADE_MAXIMA_GOTA,
            VELOCIDADE_INICIAL_GOTA + tempoPartida * INCREMENTO_VELOCIDADE_GOTA
        );

        baldeSprite.setX(MathUtils.clamp(baldeSprite.getX(), 0, cenaLarg - baldeLarg));
        baldeRet.set(baldeSprite.getX(), baldeSprite.getY(), baldeLarg, baldeAlt);

        for (int i = gotaSprites.size - 1; i >= 0; i--) {
            Sprite gotaSprite = gotaSprites.get(i);
            float gotaLarg = gotaSprite.getWidth();
            float gotaAlt = gotaSprite.getHeight();

            gotaSprite.translateY(-velocidadeGota * delta);
            gotaRet.set(gotaSprite.getX(), gotaSprite.getY(), gotaLarg, gotaAlt);

            if (gotaSprite.getY() < -gotaAlt) {
                encerrarPartida();
                return;
            } else if (baldeRet.overlaps(gotaRet)) {
                gotasColetadas++;
                gotaSprites.removeIndex(i);
                gotaSom.play();
                lblPontuacao.setText("Gotas coletadas: " + gotasColetadas);
            }
        }

        gotaTempo += delta;
        if (gotaTempo > 1f) {
            gotaTempo = 0;
            newGota();
        }
    }

    private void encerrarPartida() {
        if (partidaEncerrada) {
            return;
        }

        partidaEncerrada = true;
        musica.stop();
        mostrarFimDeJogo();
    }

    private void mostrarFimDeJogo() {
        Table overlay = new Table();
        overlay.setFillParent(true);
        overlay.center();
        overlay.setTouchable(Touchable.childrenOnly);

        Table card = UiEstilo.cardNavy(app.skin);
        card.pad(24f, 30f, 24f, 30f);

        Label titulo = new Label("Fim de jogo", app.skin, "arcade-titulo-card");
        titulo.setAlignment(Align.center);

        Label pontuacao = new Label("Gotas coletadas: " + gotasColetadas, app.skin, "arcade-corpo-card");
        pontuacao.setAlignment(Align.center);

        TextButton btnVoltar = new TextButton("Voltar ao Menu", app.skin, "arcade-botao");
        btnVoltar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sairDoJogo();
            }
        });

        card.add(titulo).growX().row();
        card.add(pontuacao).growX().padTop(12f).row();
        card.add(btnVoltar).width(180f).height(42f).padTop(20f).row();

        overlay.add(card).width(320f);
        uiStage.addActor(overlay);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        app.fViewport.apply();
        app.batch.setProjectionMatrix(app.fViewport.getCamera().combined);
        app.batch.begin();

        float cenaLarg = app.fViewport.getWorldWidth();
        float cenaAlt = app.fViewport.getWorldHeight();

        app.batch.draw(fundoTx, 0, 0, cenaLarg, cenaAlt);
        baldeSprite.draw(app.batch);

        for (Sprite gotaSprite : gotaSprites) {
            gotaSprite.draw(app.batch);
        }
        app.batch.end();
    }

    private void drawHud(float delta) {
        uiStage.getViewport().apply();
        uiStage.act(delta);
        uiStage.draw();
    }

    private void newGota() {
        float gotaLarg = 1;
        float gotaAlt = 1;
        float cenaLarg = app.fViewport.getWorldWidth();
        float cenaAlt = app.fViewport.getWorldHeight();

        Sprite gotaSprite = new Sprite(gotaTx);
        gotaSprite.setSize(gotaLarg, gotaAlt);
        gotaSprite.setX(MathUtils.random(0f, cenaLarg - gotaLarg));
        gotaSprite.setY(cenaAlt);
        gotaSprites.add(gotaSprite);
    }

    @Override
    public void resize(int width, int height) {
        app.fViewport.update(width, height, true);
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
        fundoTx.dispose();
        baldeTx.dispose();
        gotaTx.dispose();
        gotaSom.dispose();
        musica.dispose();
    }
}
