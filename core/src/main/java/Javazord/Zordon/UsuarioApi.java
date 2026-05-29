package Javazord.Zordon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public final class UsuarioApi {
    private static final String URL_BASE = "http://localhost:3000";
    private static final Json JSON = new Json();
    private static final Json READER = new Json();
    private static final Json READER_RESPONSE = new Json();
    static {
        JSON.setOutputType(JsonWriter.OutputType.json);
        READER.setIgnoreUnknownFields(true);
        READER_RESPONSE.setIgnoreUnknownFields(true);
    }

    public interface EntrarCallback {
        void sucesso(Usuario usuario);
        void erro(String mensagem);
    }

    private UsuarioApi() {}

    public static void entrar(String nome, EntrarCallback callback) {
        String corpo = JSON.toJson(new EntradaRequest(nome));

        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setUrl(URL_BASE + "/usuarios/entrar");
        request.setHeader("Content-Type", "application/json");
        request.setContent(corpo);

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                final int status = httpResponse.getStatus().getStatusCode();
                final String resposta = httpResponse.getResultAsString();

                Gdx.app.postRunnable(() -> {
                    if (status >= 200 && status < 300) {
                        UsuarioResposta dto = READER_RESPONSE.fromJson(UsuarioResposta.class, resposta);
                        callback.sucesso(new Usuario(dto.idUsuario, dto.nome, dto.pontuacaoTotal));
                    } else {
                        ErroResposta erro = tentarErro(resposta);
                        callback.erro(erro != null ? erro.erro : "Nao foi possivel entrar (HTTP " + status + ")");
                    }
                });
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.postRunnable(() ->
                    callback.erro("Servidor offline. Inicie o backend (npm start em backend-node).")
                );
            }

            @Override
            public void cancelled() {
                Gdx.app.postRunnable(() -> callback.erro("Requisicao cancelada."));
            }
        });
    }

    private static ErroResposta tentarErro(String resposta) {
        try {
            return READER.fromJson(ErroResposta.class, resposta);
        } catch (Exception e) {
            return null;
        }
    }

    private static class EntradaRequest {
        public String nome;

        EntradaRequest(String nome) {
            this.nome = nome;
        }
    }

    private static class UsuarioResposta {
        public int idUsuario;
        public String nome;
        public int pontuacaoTotal;
    }

    private static class ErroResposta {
        public String erro;
    }
}
