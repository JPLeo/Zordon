package Javazord.Zordon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class GerenciadorFontes {
    private static final String CARACTERES_PT_BR = "áàâãéêíóôõúçÁÀÂÃÉÊÍÓÔÕÚÇ";

    private GerenciadorFontes() {}

    public static void registrar(Skin skin) {
        adicionarFonte(skin, "font-display", "Fonts/Inter-Bold.ttf", 44);
        adicionarFonte(skin, "font-title", "Fonts/Inter-Bold.ttf", 26);
        adicionarFonte(skin, "font-section", "Fonts/Inter-SemiBold.ttf", 21);
        adicionarFonte(skin, "font-label", "Fonts/Inter-Regular.ttf", 16);
        adicionarFonte(skin, "font-button", "Fonts/Inter-SemiBold.ttf", 15);
        adicionarFonte(skin, "font-stat", "Fonts/Inter-Bold.ttf", 24);
    }

    private static void adicionarFonte(Skin skin, String nome, String caminho, int tamanho) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(caminho));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = tamanho;
        parameter.color = Color.WHITE;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + CARACTERES_PT_BR;

        BitmapFont fonte = generator.generateFont(parameter);
        fonte.setUseIntegerPositions(false);
        skin.add(nome, fonte, BitmapFont.class);
        generator.dispose();
    }
}
