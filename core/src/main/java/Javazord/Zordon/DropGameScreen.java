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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class DropGameScreen implements Screen {
    final AppEntrada app;

    float gotaTempo;
    Vector2 touchPos;
    int gotasColetadas;

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
        hud.pad(12f, 20f, 0f, 20f);
        hud.setTouchable(Touchable.childrenOnly);

        Table barra = new Table(app.skin);
        barra.setBackground(app.skin.getDrawable("arcade-pill-navy"));
        barra.pad(8f, 16f, 8f, 16f);

        lblPontuacao = new Label("Gotas: 0", app.skin, "arcade-hud-texto");

        TextButton btnSair = new TextButton("Sair", app.skin, "arcade-botao");
        btnSair.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sairDoJogo();
            }
        });

        barra.add(lblPontuacao).left().expandX().padRight(12f);
        barra.add(btnSair).width(96f).height(36f).right();

        hud.add(barra).growX().height(52f).row();
        uiStage.addActor(hud);
    }

    private void montarControles() {
        controleJogo = new InputAdapter() {
            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                moverBalde(x, y);
                return true;
            }

            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                moverBalde(x, y);
                return true;
            }
        };
    }

    private void moverBalde(int x, int y) {
        touchPos.set(x, y);
        app.fViewport.unproject(touchPos);
        baldeSprite.setCenterX(touchPos.x);
    }

    private void sairDoJogo() {
        musica.stop();
        app.setScreen(new MenuPrincipal(app));
        dispose();
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
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            baldeSprite.translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            baldeSprite.translateX(-speed * delta);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sairDoJogo();
        }
    }

    private void logic(float delta) {
        float cenaLarg = app.fViewport.getWorldWidth();
        float baldeLarg = baldeSprite.getWidth();
        float baldeAlt = baldeSprite.getHeight();

        baldeSprite.setX(MathUtils.clamp(baldeSprite.getX(), 0, cenaLarg - baldeLarg));
        baldeRet.set(baldeSprite.getX(), baldeSprite.getY(), baldeLarg, baldeAlt);

        for (int i = gotaSprites.size - 1; i >= 0; i--) {
            Sprite gotaSprite = gotaSprites.get(i);
            float gotaLarg = gotaSprite.getWidth();
            float gotaAlt = gotaSprite.getHeight();

            gotaSprite.translateY(-2f * delta);
            gotaRet.set(gotaSprite.getX(), gotaSprite.getY(), gotaLarg, gotaAlt);

            if (gotaSprite.getY() < -gotaAlt) {
                gotaSprites.removeIndex(i);
            } else if (baldeRet.overlaps(gotaRet)) {
                gotasColetadas++;
                gotaSprites.removeIndex(i);
                gotaSom.play();
                lblPontuacao.setText("Gotas: " + gotasColetadas);
            }
        }

        gotaTempo += delta;
        if (gotaTempo > 1f) {
            gotaTempo = 0;
            newGota();
        }
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
