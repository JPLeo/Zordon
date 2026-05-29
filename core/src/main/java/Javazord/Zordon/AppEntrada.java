/**
 * João Pedro Dias - 2025.2.0120.0007-2
 * 2026.1-CMP1223/C01 - Fundamentos de Programação
 * orientada a objetos
 */
package Javazord.Zordon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

//Main
public class AppEntrada extends Game {
    public Skin skin;
    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport fViewport;
    public ScreenViewport sViewport;
    public Usuario usuarioLogado;


    public void create(){
        fViewport = new FitViewport(8,5);
        sViewport = new ScreenViewport();

        font = new BitmapFont();
        font.setUseIntegerPositions(false);
        font.getData().setScale( 3 * (fViewport.getWorldHeight() / Gdx.graphics.getHeight()));

        skin = new Skin(Gdx.files.internal("ShadeUI/shadeui/uiskin.json"));
        UiEstilo.registrar(skin);
        batch = new SpriteBatch();

        this.setScreen(new TelaLogin(this));
    }

    public void render(){
        super.render();
    }

    public void dispose(){
        skin.dispose();
        batch.dispose();
        font.dispose();
    }
}
