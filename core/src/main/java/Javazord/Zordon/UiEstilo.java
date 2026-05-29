package Javazord.Zordon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * Paleta e componentes visuais inspirados no layout Brain Arcade (Figma).
 *
 * Drawables usam NinePatch gerado em alta resolucao para evitar bordas pixeladas
 * ao esticar pills e cards na tela.
 */
public final class UiEstilo {
    public static final Color FUNDO = Color.WHITE;
    public static final Color NAVY = Color.valueOf("0B1622");
    public static final Color NAVY_SUAVE = Color.valueOf("152536");
    public static final Color AMARELO = Color.valueOf("FFD700");
    public static final Color AMARELO_ESCURO = Color.valueOf("E6C200");
    public static final Color CINZA_BORDA = Color.valueOf("E2E8F0");
    public static final Color CINZA_FUNDO = Color.valueOf("F8FAFC");
    public static final Color TEXTO_ESCURO = Color.valueOf("0B1622");
    public static final Color TEXTO_MEDIO = Color.valueOf("64748B");
    public static final Color TEXTO_CLARO = Color.WHITE;
    public static final Color TEXTO_CARD = Color.valueOf("CBD5E1");
    public static final Color ERRO = Color.valueOf("E63946");

    /** Escala interna das texturas — maior = bordas mais suaves ao redimensionar. */
    private static final int SUPER = 4;
    private static final int ALTURA_PILL = 52;
    private static final int RAIO_PILL = 26;
    private static final int RAIO_CARD = 20;
    private static final int TAMANHO_PATCH = 96;

    private UiEstilo() {}

    public static void registrar(Skin skin) {
        registrarDrawables(skin);
        registrarLabels(skin);
        registrarCampos(skin);
        registrarBotoes(skin);
        registrarScroll(skin);
    }

    public static Label tituloApp(Skin skin, String texto) {
        Label label = new Label(texto, skin, "arcade-titulo-app");
        label.setAlignment(Align.center);
        return label;
    }

    public static Label subtituloApp(Skin skin, String texto) {
        Label label = new Label(texto, skin, "arcade-subtitulo-app");
        label.setAlignment(Align.center);
        return label;
    }

    public static Table badge(Skin skin, String texto) {
        Label label = new Label(texto, skin, "arcade-badge");
        label.setAlignment(Align.center);

        Table badge = new Table(skin);
        badge.setBackground(skin.getDrawable("arcade-pill-amarelo"));
        badge.add(label).pad(10f, 18f, 10f, 18f);
        return badge;
    }

    public static Table cardNavy(Skin skin) {
        Table card = new Table(skin);
        card.setBackground(skin.getDrawable("arcade-card-navy"));
        return card;
    }

    public static Table cardBranco(Skin skin) {
        Table card = new Table(skin);
        card.setBackground(skin.getDrawable("arcade-card-branco"));
        return card;
    }

    public static Table cardEstatistica(Skin skin, String rotulo, String valor) {
        Table card = cardBranco(skin);
        card.pad(20f);

        Label lblRotulo = new Label(rotulo, skin, "arcade-stat-rotulo");
        Label lblValor = new Label(valor, skin, "arcade-stat-valor");

        card.add(lblRotulo).left().row();
        card.add(lblValor).left().padTop(6f).row();
        return card;
    }

    public static Table cardJogo(Skin skin, String categoria, String titulo, String descricao, Runnable aoJogar) {
        Table card = cardBranco(skin);
        card.pad(28f, 32f, 28f, 32f);
        card.defaults().growX();

        Table tag = new Table(skin);
        tag.setBackground(skin.getDrawable("arcade-tag"));
        Label lblCategoria = new Label(categoria, skin, "arcade-tag-texto");
        tag.add(lblCategoria).pad(6f, 12f, 6f, 12f);

        Label lblTitulo = new Label(titulo, skin, "arcade-jogo-titulo");
        lblTitulo.setAlignment(Align.left);

        Label lblDescricao = new Label(descricao, skin, "arcade-jogo-descricao");
        lblDescricao.setWrap(true);
        lblDescricao.setAlignment(Align.left);

        TextButton btnJogar = new TextButton("Jogar agora", skin, "arcade-botao");
        btnJogar.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent event,
                                com.badlogic.gdx.scenes.scene2d.Actor actor) {
                aoJogar.run();
            }
        });

        card.add(tag).left().row();
        card.add(lblTitulo).left().padTop(16f).row();
        card.add(lblDescricao).left().padTop(10f).row();
        card.add(btnJogar).height(56f).padTop(22f).row();
        return card;
    }

    /** Scroll da pagina inteira — sem fundo que encolhe o conteudo. */
    public static ScrollPane scrollPagina(Skin skin, com.badlogic.gdx.scenes.scene2d.Actor conteudo) {
        ScrollPane scroll = new ScrollPane(conteudo, skin, "arcade-scroll-pagina");
        scroll.setFadeScrollBars(false);
        scroll.setScrollbarsVisible(true);
        scroll.setScrollingDisabled(true, false);
        scroll.setOverscroll(false, false);
        scroll.setForceScroll(false, true);
        scroll.setFlickScroll(true);
        scroll.setScrollBarPositions(false, true);
        return scroll;
    }

    private static void registrarDrawables(Skin skin) {
        skin.add("arcade-card-navy", ninePatchCard(NAVY), Drawable.class);
        skin.add("arcade-card-branco", ninePatchCardBorda(), Drawable.class);
        skin.add("arcade-pill-branco", ninePatchPill(FUNDO), Drawable.class);
        skin.add("arcade-pill-amarelo", ninePatchPill(AMARELO), Drawable.class);
        skin.add("arcade-pill-amarelo-down", ninePatchPill(AMARELO_ESCURO), Drawable.class);
        skin.add("arcade-pill-navy", ninePatchPill(NAVY), Drawable.class);
        skin.add("arcade-pill-navy-down", ninePatchPill(NAVY_SUAVE), Drawable.class);
        skin.add("arcade-tag", ninePatchPill(CINZA_FUNDO, 28, 14), Drawable.class);
        skin.add("arcade-cursor", cursorTexto(), Drawable.class);
    }

    private static void registrarLabels(Skin skin) {
        adicionarLabel(skin, "arcade-titulo-app", skin.getFont("font-title"), TEXTO_ESCURO);
        adicionarLabel(skin, "arcade-subtitulo-app", skin.getFont("font-label"), TEXTO_MEDIO);
        adicionarLabel(skin, "arcade-titulo-card", skin.getFont("font-title"), TEXTO_CLARO);
        adicionarLabel(skin, "arcade-corpo-card", skin.getFont("font-label"), TEXTO_CARD);
        adicionarLabel(skin, "arcade-secao", skin.getFont("font-title"), TEXTO_ESCURO);
        adicionarLabel(skin, "arcade-badge", skin.getFont("font-button"), TEXTO_ESCURO);
        adicionarLabel(skin, "arcade-stat-rotulo", skin.getFont("font-label"), TEXTO_MEDIO);
        adicionarLabel(skin, "arcade-stat-valor", skin.getFont("font-title"), TEXTO_ESCURO);
        adicionarLabel(skin, "arcade-jogo-titulo", skin.getFont("font-title"), TEXTO_ESCURO);
        adicionarLabel(skin, "arcade-jogo-descricao", skin.getFont("font-label"), TEXTO_MEDIO);
        adicionarLabel(skin, "arcade-tag-texto", skin.getFont("font-label"), TEXTO_ESCURO);
        adicionarLabel(skin, "arcade-hud-texto", skin.getFont("font-button"), TEXTO_CLARO);
        adicionarLabel(skin, "arcade-erro", skin.getFont("font-label"), ERRO);
        adicionarLabel(skin, "arcade-status", skin.getFont("font-label"), TEXTO_MEDIO);
    }

    private static void adicionarLabel(Skin skin, String nome, BitmapFont fonte, Color cor) {
        Label.LabelStyle estilo = new Label.LabelStyle(fonte, cor);
        skin.add(nome, estilo);
    }

    private static void registrarCampos(Skin skin) {
        TextField.TextFieldStyle padrao = skin.get(TextField.TextFieldStyle.class);
        TextField.TextFieldStyle campo = new TextField.TextFieldStyle(padrao);
        campo.font = skin.getFont("font-label");
        campo.fontColor = TEXTO_ESCURO;
        campo.messageFontColor = TEXTO_MEDIO;
        campo.background = skin.getDrawable("arcade-pill-branco");
        campo.cursor = skin.getDrawable("arcade-cursor");
        campo.selection = padrao.selection;
        skin.add("arcade-campo-texto", campo);
    }

    private static void registrarBotoes(Skin skin) {
        TextButton.TextButtonStyle primario = new TextButton.TextButtonStyle();
        primario.font = skin.getFont("font-button");
        primario.fontColor = TEXTO_ESCURO;
        primario.downFontColor = TEXTO_ESCURO;
        primario.up = skin.getDrawable("arcade-pill-amarelo");
        primario.down = skin.getDrawable("arcade-pill-amarelo-down");
        primario.disabled = skin.getDrawable("arcade-tag");
        skin.add("arcade-botao", primario);

        TextButton.TextButtonStyle secundario = new TextButton.TextButtonStyle();
        secundario.font = skin.getFont("font-button");
        secundario.fontColor = TEXTO_CLARO;
        secundario.downFontColor = TEXTO_CLARO;
        secundario.up = skin.getDrawable("arcade-pill-navy");
        secundario.down = skin.getDrawable("arcade-pill-navy-down");
        skin.add("arcade-botao-navy", secundario);
    }

    private static void registrarScroll(Skin skin) {
        skin.add("arcade-scroll-pagina", new ScrollPane.ScrollPaneStyle());
    }

    /** Pill horizontal: so estica o centro, mantendo as pontas arredondadas. */
    private static NinePatchDrawable ninePatchPill(Color cor) {
        return ninePatchPill(cor, ALTURA_PILL, RAIO_PILL);
    }

    private static NinePatchDrawable ninePatchPill(Color cor, int altura, int raio) {
        int h = altura * SUPER;
        int r = raio * SUPER;
        int w = Math.max(h, (raio * 2 + 16) * SUPER);

        Pixmap pixmap = pixmapTransparente(w, h);
        preencherArredondado(pixmap, cor, 0, 0, w, h, r);

        return criarNinePatch(pixmap, r, r, 0, 0);
    }

    /** Card com cantos arredondados em todas as direcoes. */
    private static NinePatchDrawable ninePatchCard(Color cor) {
        int t = TAMANHO_PATCH * SUPER;
        int r = RAIO_CARD * SUPER;

        Pixmap pixmap = pixmapTransparente(t, t);
        preencherArredondado(pixmap, cor, 0, 0, t, t, r);

        return criarNinePatch(pixmap, r, r, r, r);
    }

    private static NinePatchDrawable ninePatchCardBorda() {
        int t = TAMANHO_PATCH * SUPER;
        int r = RAIO_CARD * SUPER;
        int b = 2 * SUPER;

        Pixmap pixmap = pixmapTransparente(t, t);
        preencherArredondado(pixmap, CINZA_BORDA, 0, 0, t, t, r);
        preencherArredondado(pixmap, FUNDO, b, b, t - 2 * b, t - 2 * b, Math.max(0, r - b));

        return criarNinePatch(pixmap, r, r, r, r);
    }

    /** Cursor fino do campo de texto — nao usar pill como cursor. */
    private static Drawable cursorTexto() {
        Pixmap pixmap = new Pixmap(2 * SUPER, 22 * SUPER, Pixmap.Format.RGBA8888);
        pixmap.setColor(TEXTO_ESCURO);
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());

        Texture textura = new Texture(pixmap);
        pixmap.dispose();
        textura.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return new TextureRegionDrawable(new TextureRegion(textura));
    }

    private static Pixmap pixmapTransparente(int largura, int altura) {
        Pixmap pixmap = new Pixmap(largura, altura, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        return pixmap;
    }

    private static NinePatchDrawable criarNinePatch(Pixmap pixmap, int esq, int dir, int topo, int base) {
        Texture textura = new Texture(pixmap);
        pixmap.dispose();
        textura.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        NinePatch patch = new NinePatch(new TextureRegion(textura), esq, dir, topo, base);
        return new NinePatchDrawable(patch);
    }

    private static void preencherArredondado(Pixmap pixmap, Color cor, int x, int y, int largura, int altura, int raio) {
        pixmap.setColor(cor);
        if (raio <= 0) {
            pixmap.fillRectangle(x, y, largura, altura);
            return;
        }
        int r = Math.min(raio, Math.min(largura, altura) / 2);
        pixmap.fillRectangle(x + r, y, largura - 2 * r, altura);
        pixmap.fillRectangle(x, y + r, largura, altura - 2 * r);
        pixmap.fillCircle(x + r, y + r, r);
        pixmap.fillCircle(x + largura - r - 1, y + r, r);
        pixmap.fillCircle(x + r, y + altura - r - 1, r);
        pixmap.fillCircle(x + largura - r - 1, y + altura - r - 1, r);
    }
}
