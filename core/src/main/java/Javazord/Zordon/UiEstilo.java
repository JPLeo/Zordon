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
 * Paleta e componentes visuais para uma UI mais limpa e retangular.
 *
 * Drawables usam NinePatch gerado em alta resolucao para evitar bordas pixeladas
 * ao esticar pills e cards na tela.
 */
public final class UiEstilo {
    public static final Color FUNDO = Color.valueOf("F3F4F6");
    public static final Color NAVY = Color.valueOf("111827");
    public static final Color NAVY_SUAVE = Color.valueOf("1F2937");
    public static final Color PRIMARIO = Color.valueOf("2563EB");
    public static final Color PRIMARIO_ESCURO = Color.valueOf("1D4ED8");
    public static final Color PRIMARIO_CLARO = Color.valueOf("DBEAFE");
    public static final Color CINZA_BORDA = Color.valueOf("D1D5DB");
    public static final Color CINZA_FUNDO = Color.valueOf("F9FAFB");
    public static final Color TEXTO_ESCURO = Color.valueOf("111827");
    public static final Color TEXTO_MEDIO = Color.valueOf("4B5563");
    public static final Color TEXTO_CLARO = Color.WHITE;
    public static final Color TEXTO_CARD = Color.valueOf("E5E7EB");
    public static final Color ERRO = Color.valueOf("E63946");
    public static final Color VERDE = Color.valueOf("22C55E");
    public static final Color VERMELHO = Color.valueOf("EF4444");
    public static final Color ROXO = Color.valueOf("7C3AED");
    public static final Color BOTAO_SECUNDARIO = Color.valueOf("374151");
    public static final Color BOTAO_SECUNDARIO_DOWN = Color.valueOf("1F2937");

    /** Escala interna das texturas — maior = bordas mais suaves ao redimensionar. */
    private static final int SUPER = 4;
    private static final int ALTURA_CONTROLE = 42;
    private static final int RAIO_CONTROLE = 6;
    private static final int RAIO_TAG = 6;
    private static final int RAIO_CARD = 8;
    private static final int TAMANHO_PATCH = 96;

    private UiEstilo() {}

    public static void registrar(Skin skin) {
        prepararFontes(skin);
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

    public static Table marcaApp(Skin skin, String texto) {
        Table marca = new Table();
        marca.defaults().spaceRight(12f);

        Table simbolo = new Table(skin);
        simbolo.setBackground(skin.getDrawable("arcade-brand-mark"));
        Label letra = new Label("Z", skin, "arcade-brand-mark-texto");
        letra.setAlignment(Align.center);
        simbolo.add(letra).width(36f).height(36f);

        marca.add(simbolo).width(36f).height(36f);
        marca.add(tituloApp(skin, texto)).left();
        return marca;
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
        badge.add(label).pad(7f, 14f, 7f, 14f);
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
        card.pad(12f, 14f, 12f, 14f);

        Label lblRotulo = new Label(rotulo, skin, "arcade-stat-rotulo");
        Label lblValor = new Label(valor, skin, "arcade-stat-valor");

        card.add(lblRotulo).left().row();
        card.add(lblValor).left().padTop(6f).row();
        return card;
    }

    public static Table cardJogo(Skin skin, String categoria, String titulo, String descricao, String textoBotao, Runnable aoJogar) {
        Table card = cardBranco(skin);
        card.pad(30f, 34f, 30f, 34f);
        card.defaults().growX();

        Label lblCategoria = new Label(categoria, skin, "arcade-tag-texto");
        lblCategoria.setAlignment(Align.left);

        Label lblTitulo = new Label(titulo, skin, "arcade-jogo-titulo");
        lblTitulo.setAlignment(Align.left);

        Label lblDescricao = new Label(descricao, skin, "arcade-jogo-descricao");
        lblDescricao.setWrap(true);
        lblDescricao.setAlignment(Align.left);

        TextButton btnJogar = new TextButton(textoBotao, skin, "arcade-botao");
        btnJogar.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent event,
                                com.badlogic.gdx.scenes.scene2d.Actor actor) {
                aoJogar.run();
            }
        });

        card.add(lblCategoria).left().row();
        card.add(lblTitulo).left().padTop(8f).row();
        card.add(lblDescricao).left().padTop(8f).row();
        card.add(btnJogar).width(168f).height(42f).left().padTop(20f).row();
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
        skin.add("arcade-pill-branco", ninePatchControleComBorda(Color.WHITE), Drawable.class);
        skin.add("arcade-pill-amarelo", ninePatchControle(PRIMARIO), Drawable.class);
        skin.add("arcade-pill-amarelo-down", ninePatchControle(PRIMARIO_ESCURO), Drawable.class);
        skin.add("arcade-pill-navy", ninePatchControle(BOTAO_SECUNDARIO), Drawable.class);
        skin.add("arcade-pill-navy-down", ninePatchControle(BOTAO_SECUNDARIO_DOWN), Drawable.class);
        skin.add("arcade-pill-azul", ninePatchControle(PRIMARIO), Drawable.class);
        skin.add("arcade-pill-azul-down", ninePatchControle(PRIMARIO_ESCURO), Drawable.class);
        skin.add("arcade-pill-verde", ninePatchControle(VERDE), Drawable.class);
        skin.add("arcade-pill-vermelho", ninePatchControle(VERMELHO), Drawable.class);
        skin.add("arcade-pill-resposta", ninePatchControleComBorda(Color.WHITE), Drawable.class);
        skin.add("arcade-pill-resposta-down", ninePatchControleComBorda(PRIMARIO_CLARO), Drawable.class);
        skin.add("arcade-celula-tabuleiro", ninePatchControleComBorda(Color.WHITE, 72, 8), Drawable.class);
        skin.add("arcade-celula-tabuleiro-down", ninePatchControleComBorda(PRIMARIO_CLARO, 72, 8), Drawable.class);
        skin.add("arcade-tag", ninePatchControle(CINZA_FUNDO, 28, RAIO_TAG), Drawable.class);
        skin.add("arcade-brand-mark", ninePatchControle(PRIMARIO, 36, 8), Drawable.class);
        skin.add("arcade-cursor", cursorTexto(), Drawable.class);
    }

    private static void prepararFontes(Skin skin) {
        suavizarFonte(skin.getFont("font-title"));
        suavizarFonte(skin.getFont("font-label"));
        suavizarFonte(skin.getFont("font-button"));
    }

    private static void suavizarFonte(BitmapFont fonte) {
        fonte.setUseIntegerPositions(false);
        for (TextureRegion regiao : fonte.getRegions()) {
            regiao.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
    }

    private static void registrarLabels(Skin skin) {
        adicionarLabel(skin, "arcade-titulo-app", skin.getFont("font-display"), TEXTO_ESCURO);
        adicionarLabel(skin, "arcade-subtitulo-app", skin.getFont("font-label"), TEXTO_MEDIO);
        adicionarLabel(skin, "arcade-titulo-card", skin.getFont("font-title"), TEXTO_CLARO);
        adicionarLabel(skin, "arcade-corpo-card", skin.getFont("font-label"), TEXTO_CARD);
        adicionarLabel(skin, "arcade-secao", skin.getFont("font-section"), TEXTO_ESCURO);
        adicionarLabel(skin, "arcade-badge", skin.getFont("font-button"), TEXTO_CLARO);
        adicionarLabel(skin, "arcade-stat-rotulo", skin.getFont("font-label"), TEXTO_MEDIO);
        adicionarLabel(skin, "arcade-stat-valor", skin.getFont("font-stat"), TEXTO_ESCURO);
        adicionarLabel(skin, "arcade-jogo-titulo", skin.getFont("font-section"), TEXTO_ESCURO);
        adicionarLabel(skin, "arcade-jogo-descricao", skin.getFont("font-label"), TEXTO_MEDIO);
        adicionarLabel(skin, "arcade-tag-texto", skin.getFont("font-button"), TEXTO_MEDIO);
        adicionarLabel(skin, "arcade-hud-texto", skin.getFont("font-button"), TEXTO_CLARO);
        adicionarLabel(skin, "arcade-erro", skin.getFont("font-label"), ERRO);
        adicionarLabel(skin, "arcade-status", skin.getFont("font-label"), TEXTO_MEDIO);
        adicionarLabel(skin, "arcade-brand-mark-texto", skin.getFont("font-button"), TEXTO_CLARO);
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
        primario.fontColor = TEXTO_CLARO;
        primario.downFontColor = TEXTO_CLARO;
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

        TextButton.TextButtonStyle azul = new TextButton.TextButtonStyle();
        azul.font = skin.getFont("font-button");
        azul.fontColor = TEXTO_CLARO;
        azul.downFontColor = TEXTO_CLARO;
        azul.up = skin.getDrawable("arcade-pill-azul");
        azul.down = skin.getDrawable("arcade-pill-azul-down");
        azul.disabled = skin.getDrawable("arcade-tag");
        skin.add("arcade-botao-azul", azul);

        TextButton.TextButtonStyle resposta = new TextButton.TextButtonStyle();
        resposta.font = skin.getFont("font-button");
        resposta.fontColor = TEXTO_ESCURO;
        resposta.downFontColor = PRIMARIO;
        resposta.up = skin.getDrawable("arcade-pill-resposta");
        resposta.down = skin.getDrawable("arcade-pill-resposta-down");
        resposta.disabled = skin.getDrawable("arcade-tag");
        skin.add("arcade-botao-resposta", resposta);

        TextButton.TextButtonStyle correto = new TextButton.TextButtonStyle();
        correto.font = skin.getFont("font-button");
        correto.fontColor = TEXTO_CLARO;
        correto.downFontColor = TEXTO_CLARO;
        correto.up = skin.getDrawable("arcade-pill-verde");
        correto.down = skin.getDrawable("arcade-pill-verde");
        correto.disabled = skin.getDrawable("arcade-pill-verde");
        skin.add("arcade-botao-correto", correto);

        TextButton.TextButtonStyle incorreto = new TextButton.TextButtonStyle();
        incorreto.font = skin.getFont("font-button");
        incorreto.fontColor = TEXTO_CLARO;
        incorreto.downFontColor = TEXTO_CLARO;
        incorreto.up = skin.getDrawable("arcade-pill-vermelho");
        incorreto.down = skin.getDrawable("arcade-pill-vermelho");
        incorreto.disabled = skin.getDrawable("arcade-pill-vermelho");
        skin.add("arcade-botao-incorreto", incorreto);

        TextButton.TextButtonStyle celula = new TextButton.TextButtonStyle();
        celula.font = skin.getFont("font-display");
        celula.fontColor = TEXTO_MEDIO;
        celula.downFontColor = PRIMARIO;
        celula.disabledFontColor = TEXTO_MEDIO;
        celula.up = skin.getDrawable("arcade-celula-tabuleiro");
        celula.down = skin.getDrawable("arcade-celula-tabuleiro-down");
        celula.disabled = skin.getDrawable("arcade-celula-tabuleiro");
        skin.add("arcade-celula-tabuleiro", celula);

        TextButton.TextButtonStyle celulaX = new TextButton.TextButtonStyle(celula);
        celulaX.fontColor = PRIMARIO;
        celulaX.disabledFontColor = PRIMARIO;
        skin.add("arcade-celula-x", celulaX);

        TextButton.TextButtonStyle celulaO = new TextButton.TextButtonStyle(celula);
        celulaO.fontColor = ROXO;
        celulaO.disabledFontColor = ROXO;
        skin.add("arcade-celula-o", celulaO);
    }

    private static void registrarScroll(Skin skin) {
        skin.add("arcade-scroll-pagina", new ScrollPane.ScrollPaneStyle());
    }

    /** Mantem compatibilidade com os nomes antigos, agora com raio menor. */
    private static NinePatchDrawable ninePatchPill(Color cor) {
        return ninePatchControle(cor);
    }

    private static NinePatchDrawable ninePatchControle(Color cor) {
        return ninePatchControle(cor, ALTURA_CONTROLE, RAIO_CONTROLE);
    }

    private static NinePatchDrawable ninePatchControle(Color cor, int altura, int raio) {
        int h = altura * SUPER;
        int r = raio * SUPER;
        int w = Math.max(h, (raio * 2 + 24) * SUPER);

        Pixmap pixmap = pixmapTransparente(w, h);
        preencherArredondado(pixmap, cor, 0, 0, w, h, r);

        return criarNinePatch(pixmap, r, r, r, r);
    }

    private static NinePatchDrawable ninePatchControleComBorda(Color cor) {
        return ninePatchControleComBorda(cor, ALTURA_CONTROLE, RAIO_CONTROLE);
    }

    private static NinePatchDrawable ninePatchControleComBorda(Color cor, int altura, int raio) {
        int h = altura * SUPER;
        int r = raio * SUPER;
        int b = 1 * SUPER;
        int w = Math.max(h, (raio * 2 + 24) * SUPER);

        Pixmap pixmap = pixmapTransparente(w, h);
        preencherArredondado(pixmap, CINZA_BORDA, 0, 0, w, h, r);
        preencherArredondado(pixmap, cor, b, b, w - 2 * b, h - 2 * b, Math.max(0, r - b));

        return criarNinePatch(pixmap, r, r, r, r);
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
        preencherArredondado(pixmap, Color.WHITE, b, b, t - 2 * b, t - 2 * b, Math.max(0, r - b));

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
