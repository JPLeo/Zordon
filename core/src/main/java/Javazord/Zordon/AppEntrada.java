package Javazord.Zordon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class AppEntrada extends Game {
    public Skin skin;
    public SpriteBatch batch;
    public FitViewport fViewport;
    public ScreenViewport sViewport;
    public Usuario usuarioLogado;

    @Override
    public void create() {
        fViewport = new FitViewport(8, 5);
        sViewport = new ScreenViewport();

        skin = new Skin(Gdx.files.internal("ShadeUI/shadeui/uiskin.json"));
        GerenciadorFontes.registrar(skin);
        UiEstilo.registrar(skin);
        batch = new SpriteBatch();

        setScreen(new TelaLogin(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        skin.dispose();
        batch.dispose();
    }
}
