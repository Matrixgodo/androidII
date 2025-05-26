package com.diarioescolar;

import android.net.SSLCertificateSocketFactory;

import com.diarioescolar.modelo.Estudante;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class Conexao {
    private static final boolean MODO_TESTE = true;
    private final Gson gson;

    public Conexao() {
        this.gson = new Gson();
    }

    /**
     * Cria e configura uma conexão HTTP ou HTTPS com os parâmetros especificados.
     *
     * <p>Este método estabelece uma conexão HTTP com base na URL e no método HTTP informados.
     * Caso a conexão seja HTTPS e o sistema esteja em modo de teste (MODO_TESTE = true),
     * a verificação de certificado e hostname será configurada para aceitar todos os certificados,
     * permitindo testes com servidores que usam certificados autoassinados.</p>
     *
     * @param urlEndereco Endereço completo da URL para onde será feita a requisição.
     * @param metodo Método HTTP a ser utilizado na requisição (por exemplo: "GET", "POST", etc.).
     * @return Um objeto {@link HttpURLConnection} pronto para envio de dados ou leitura da resposta.
     * @throws IOException Se ocorrerem erros ao abrir ou configurar a conexão.
     */
    private HttpURLConnection criarConexao(String urlEndereco, String metodo) throws IOException {
        // Cria um objeto URL a partir do endereço informado
        URL url = new URL(urlEndereco);

        // Abre a conexão e obtém uma instância de HttpURLConnection
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

        // Caso a conexão seja HTTPS e esteja em modo de teste, aplica configuração personalizada
        if (conexao instanceof HttpsURLConnection && MODO_TESTE) {
            configurarHttpsConexao((HttpsURLConnection) conexao);
        }

        // Define o método HTTP (ex: GET, POST, PUT, DELETE)
        conexao.setRequestMethod(metodo);

        // Reutilizar conexões HTTP persistentes (Keep-Alive) : Keep-Alive evita reabertura de sockets TCP (ganho em rede)
        conexao.setRequestProperty("Connection", "keep-alive"); // Reduz o overhead de estabelecer novas conexões TCP em múltiplas requisições.

        // Ajuda no rastreamento e evita falhas em APIs que exigem esse cabeçalho.
        conexao.setRequestProperty("User-Agent", "Diario_Estudantes/1.0"); // Alguns servidores rejeitam conexões sem User-Agent válido. Também é útil para identificar meu app

        // Desabilite o uso de cache local se você precisa sempre de dados atualizados
        conexao.setUseCaches(false);

        // Adicionar suporte a compressão GZIP (se o servidor suportar) : GZIP reduz tráfego (menos RAM e dados transferidos).
        //conexao.setRequestProperty("Accept-Encoding", "gzip"); // Reduz o tráfego de dados -  lembre-se de descomprimir a resposta se ela vier compactada

        // Define tempo máximo de espera para conectar (5 segundos)
        conexao.setConnectTimeout(5000);

        // Define tempo máximo de espera para leitura da resposta (5 segundos)
        conexao.setReadTimeout(5000);

        // Retorna a conexão configurada
        return conexao;
    }


    /**
     * Configura uma conexão HTTPS de forma insegura, ignorando completamente a verificação do certificado SSL
     * e a verificação do nome do host (hostname).
     *
     * <p><b>IMPORTANTE:</b> Este método deve ser usado apenas para fins de testes ou em ambientes de desenvolvimento.</p>
     * <p>Ignorar a verificação de certificados e hostnames pode abrir brechas de segurança graves,
     * como ataques man-in-the-middle (MITM).</p>
     *
     * @param httpsCon A conexão HTTPS que será configurada para ignorar a verificação de segurança.
     */
    private void configurarHttpsConexao(HttpsURLConnection httpsCon) {
        // Define uma fábrica de soquetes SSL que não valida certificados.
        // Esta fábrica permite conexões com qualquer servidor, mesmo que o certificado não seja confiável ou esteja expirado.
        httpsCon.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));

        // Define um verificador de hostname que aceita qualquer nome de host, ignorando a validação padrão.
        // Isso significa que o nome do host no certificado pode não coincidir com o endereço do servidor.
        httpsCon.setHostnameVerifier((hostname, session) -> true);
    }

    /**
     * Executa uma requisição HTTP GET e retorna um InputStream bufferizado
     * com tamanho de buffer otimizado para leitura de grandes volumes de dados.
     *
     * @param urlEndereco URL do recurso a ser acessado.
     * @return InputStream com buffer de 16 KB para leitura eficiente.
     * @throws RuntimeException em caso de falha de conexão ou leitura.
     */
    public InputStream get(String urlEndereco) {
        try {
            HttpURLConnection conexao = criarConexao(urlEndereco, "GET");

            // Cria um buffer de 16 KB para reduzir chamadas ao SO e melhorar leitura sequencial
            BufferedInputStream bufferedInputStream = new BufferedInputStream(conexao.getInputStream(), 16 * 1024);

            return bufferedInputStream; // Responsabilidade de fechar é do consumidor
        } catch (IOException e) {
            throw new RuntimeException("Erro ao conectar à URL: " + urlEndereco, e);
        }
    }

    /**
     * Envia um objeto Estudante para o endpoint especificado via requisição HTTP PUT,
     * serializando o conteúdo em JSON e retornando o InputStream da resposta.
     *
     * @param urlEndereco URL do recurso no servidor que receberá a atualização.
     * @param estudante Objeto Estudante a ser enviado no corpo da requisição.
     * @return InputStream da resposta da requisição, para processamento posterior.
     * @throws RuntimeException em caso de falha de conexão ou IO.
     */
    public InputStream enviarEstudanteViaPut(String urlEndereco, Estudante estudante) {
        try {
            HttpURLConnection conexao = criarConexao(urlEndereco, "PUT");

            // Permite envio de dados no corpo da requisição
            conexao.setDoOutput(true);

            // Define o tipo de conteúdo como JSON (UTF-8 é o padrão seguro)
            conexao.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Serializa o objeto Estudante para JSON
            String jsonCorpo = gson.toJson(estudante);

            // Escreve o JSON no corpo da requisição (com buffer)
            escreverNoCorpo(conexao, jsonCorpo);

            // Lê a resposta da requisição (InputStream pode ser processado fora)
            return new BufferedInputStream(conexao.getInputStream(), 16 * 1024); //Leitura de resposta mais rápida e com menos chamadas ao SO
        } catch (IOException e) {
            throw new RuntimeException("Erro ao enviar estudante via PUT: " + urlEndereco, e);
        }
    }


    /**
     * Escreve dados no corpo da requisição de forma eficiente usando UTF-8.
     *
     * @param conexao Conexão HTTP ativa com saída habilitada.
     * @param conteudo Conteúdo a ser escrito no corpo da requisição.
     * @throws IOException em caso de falha de escrita.
     */
    private void escreverNoCorpo(HttpURLConnection conexao, String conteudo) throws IOException {
        try (OutputStream output = conexao.getOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(writer, 16 * 1024)) { // 	Escrita mais eficiente no corpo da requisição

            bufferedWriter.write(conteudo);
            bufferedWriter.flush(); // garante envio imediato
        }
    }



    /**
     * Realiza uma requisição HTTP DELETE para remover um estudante,
     * utilizando o ID contido na URL.
     *
     * @param urlEndereco URL do endpoint com o ID do estudante a ser excluído.
     * @return {@code true} se a exclusão foi bem-sucedida (status HTTP 2xx), {@code false} caso contrário.
     * @throws RuntimeException em caso de falha na conexão ou erro inesperado de rede.
     */
    public boolean excluirEstudante(String urlEndereco) {
        try {
            // Cria e configura a conexão HTTP no modo DELETE
            HttpURLConnection conexao = criarConexao(urlEndereco, "DELETE");

            // Obtém o código de status da resposta HTTP
            int status = conexao.getResponseCode();

            // Retorna verdadeiro se o status for 2xx (sucesso)
            return status >= 200 && status < 300;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao excluir estudante via DELETE: " + urlEndereco, e);
        }
    }




    /**
     * Envia os dados de um estudante em formato JSON via requisição HTTP POST para cadastro no servidor.
     *
     * @param urlEndereco URL do endpoint responsável pelo cadastro.
     * @param jsonCorpo String JSON representando o estudante a ser cadastrado.
     * @return {@code true} se o estudante foi cadastrado com sucesso (status HTTP 2xx), {@code false} caso contrário.
     * @throws RuntimeException em caso de falha de conexão ou erro inesperado.
     */
    public boolean cadastrarEstudante(String urlEndereco, String jsonCorpo) {
        HttpURLConnection conexao = null;
        try {
            // Cria a conexão e define o método POST
            conexao = criarConexao(urlEndereco, "POST");

            // Permite envio de dados no corpo da requisição
            conexao.setDoOutput(true);

            // Define o tipo de conteúdo como JSON
            conexao.setRequestProperty("Content-Type", "application/json");

            // Escreve o corpo JSON na conexão
            escreverNoCorpo(conexao, jsonCorpo);

            // Envia a requisição e obtém o status de resposta
            int status = conexao.getResponseCode();

            // Considera sucesso se status for HTTP 2xx
            return status >= 200 && status < 300;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao enviar estudante via POST: " + urlEndereco, e);
        } finally {
            if (conexao != null) {
                conexao.disconnect(); // Libera os recursos explicitamente
            }
        }
    }

    /**
     * Converte um InputStream (fluxo de bytes) em uma única String.
     * Ideal para leitura de respostas HTTP em formato textual (ex: JSON).
     *
     * @param is InputStream a ser convertido (ex: conexão HTTP).
     * @return String contendo todo o conteúdo lido do InputStream.
     * @throws RuntimeException em caso de falha na leitura do fluxo.
     */
    public String converterParaTexto(InputStream is) {
        // Utiliza UTF-8 explicitamente para evitar problemas de codificação
        try (   InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder sb = new StringBuilder();
            String linha;

            // Lê linha por linha e acumula no StringBuilder
            while ((linha = bufferedReader.readLine()) != null) {
                sb.append(linha).append("\n");
            }

            return sb.toString();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o stream de entrada.", e);
        }
    }
}
