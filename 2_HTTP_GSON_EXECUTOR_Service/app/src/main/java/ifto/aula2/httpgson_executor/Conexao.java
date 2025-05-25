package ifto.aula2.httpgson_executor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Conexao { // retrofit
    public InputStream obterRespostaHTTP(String url) {
        try {
            /**
             * Representa um URL — Localizador Uniforme de Recursos (Uniform Resource Locator),
             * que funciona como um "endereço" para localizar recursos disponíveis na internet (World Wide Web).
             *
             * Um recurso pode ser algo simples, como um arquivo HTML, uma imagem ou um diretório,
             * ou algo mais complexo, como uma API, uma consulta a banco de dados, ou um mecanismo de busca.
             *
             * A classe URL permite acessar e manipular essas informações de forma estruturada (protocolo, host, caminho, etc).
             *
             * Exemplo de formatos válidos:
             * - http://www.exemplo.com/index.html
             * - https://api.site.com/usuarios?id=10
             * - ftp://servidor.com/arquivo.txt
             *
             * Para mais detalhes sobre os tipos de URL e seus formatos, consulte:
             * {@link <a href="http://web.archive.org/web/20051219043731/http://archive.ncsa.uiuc.edu/SDG/Software/Mosaic/Demo/url-primer.html">este guia de URLs e seus formatos</a>}.
             */
            URL urlConexao = new URL(url);//Cria o objeto URL

            /**
             * Retorna uma instância de HttpURLConnection  que representa uma conexão HTTP com o objeto remoto referenciado pela URL.
             *
             * Embora o método openConnection() da classe URL retorne uma instância da classe URLConnection (que é genérica),
             * neste caso, sei que a URL utilizada representa uma conexão HTTP (por exemplo, começa com "http://" ou "https://").
             *
             * A classe URLConnection é abstrata e serve como superclasse para diferentes tipos de conexões, como HTTP, FTP, etc.
             *
             * Como desejo trabalhar com funcionalidades específicas do protocolo HTTP — como definir o método da requisição (GET, POST),
             * configurar cabeçalhos (headers), e obter o código de resposta HTTP — faço um "cast" para HttpURLConnection.
             *
             * HttpURLConnection é uma subclasse de URLConnection voltada exclusivamente para conexões HTTP,
             * permitindo acessar métodos que não estão disponíveis na superclasse.
             *
             * Com isso, tenho acesso completo às ferramentas necessárias para manipular a conexão HTTP de forma apropriada.
             */
            HttpURLConnection conexao = (HttpURLConnection) urlConexao.openConnection(); // prepara a conexao logo apos criar a url mas nao realiza a conexao

            /** Defina o método para a solicitação de URL (Define o método HTTP da requisição), um dos seguintes: GET POST HEAD OPTIONS PUT DELETE TRACE são legais, sujeitos a restrições de protocolo.
             *
             *  Cada método tem um propósito diferente na comunicação HTTP:
             *  - GET: Recupera dados do servidor (sem corpo de requisição).
             *  - POST: Envia dados ao servidor (usado, por exemplo, para formulários).
             *  - PUT: Atualiza completamente um recurso.
             *  - DELETE: Remove um recurso.
             *  - HEAD: Recupera apenas os cabeçalhos da resposta (sem o corpo).
             *  - OPTIONS: Solicita os métodos suportados pelo servidor.
             *  - TRACE: Ecoa a requisição recebida (pouco usado).
             *  Importante: Nem todos os servidores aceitam todos os métodos — isso depende das permissões e da implementação no backend.
             */
            conexao.setRequestMethod("GET");// Define parâmetros da requisição


            /**
             * BufferedInputStream é uma classe do Java que adiciona uma camada de **buffer** (memória temporária)
             * sobre um InputStream existente — como o que obtemos ao chamar `conexao.getInputStream()`.
             *
             *  O que é "buffer" aqui?
             * É uma área de armazenamento temporária em memória que permite **ler dados em blocos maiores de uma vez**,
             * ao invés de ler **byte por byte** diretamente da fonte (como um arquivo ou uma conexão).
             *
             *  Benefícios do uso:
             * - Melhora a **performance da leitura**, reduzindo o número de acessos à fonte de dados.
             * - Oferece suporte aos métodos `mark()` e `reset()`, que permitem marcar um ponto no fluxo e retornar a ele depois.
             *
             * 🛠 Como funciona:
             * - Quando o `BufferedInputStream` é criado, ele aloca um **array interno de bytes** (o buffer).
             * - À medida que os dados são lidos com `.read()`, o buffer é preenchido com **vários bytes de uma só vez**.
             * - Quando o buffer esvazia, ele é automaticamente reabastecido a partir do fluxo original (`InputStream`).
             *
             *  Dica:
             * - Use `BufferedInputStream` quando for ler **grandes quantidades de dados** (como arquivos, respostas HTTP ou streams da rede).
             * - O tamanho padrão do buffer é 8192 bytes, mas você pode definir outro se quiser: `new BufferedInputStream(stream, tamanhoBuffer)`.
             *
             * Sobre `mark()` e `reset()`:
             * - `mark(int readlimit)`: marca um ponto no fluxo. Pode usar `reset()` depois para voltar a esse ponto.
             * - Útil em situações em que você quer "testar" os dados antes de continuar, como ao identificar o tipo de conteúdo recebido.
             *
             * Exemplo:
             * return new BufferedInputStream(conexao.getInputStream());
             * Aqui, a conexão HTTP fornece um InputStream e o BufferedInputStream melhora o desempenho da leitura.
             */
            //getInputStream() retorna um InputStream que representa o corpo da resposta HTTP recebida da conexão estabelecida. - Envia a requisição, abre conexão real, retorna dados
            return new BufferedInputStream(conexao.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // Tratamento de exceção para erros de entrada/saída
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método responsável por converter o conteúdo de um InputStream (fluxo de entrada de dados, como resposta de uma requisição)
     * em uma única String, com quebras de linha preservadas.
     *
     * Esse tipo de leitura é comum ao lidar com APIs, leitura de arquivos ou comunicação em rede.
     */
    public String converter(InputStream inputStream) {
        // InputStreamReader é usado para converter o fluxo de bytes (InputStream) em caracteres (leitura de texto).
        // Ele atua como uma "ponte" entre dados binários e texto legível.
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        // BufferedReader adiciona um buffer (área temporária de leitura) ao InputStreamReader.
        // Isso torna a leitura mais eficiente, pois lê blocos de dados em vez de caractere por caractere.
        // Também permite utilizar o método readLine(), que lê linha por linha.
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        // StringBuilder é usado para construir a String final de forma eficiente, evitando uso de concatenação com "+".
        StringBuilder stringBuilder = new StringBuilder();

        String linha; // Variável para armazenar cada linha lida do BufferedReader.
        try {
            // Enquanto ainda houver linhas para ler (readLine() retorna null no final do fluxo),
            // adiciona a linha lida ao StringBuilder e também uma quebra de linha (\n), pois readLine() remove isso.
            while ((linha = bufferedReader.readLine()) != null) {
                stringBuilder.append(linha);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            // Captura qualquer exceção de entrada/saída que possa ocorrer durante a leitura do fluxo.
            e.printStackTrace();
            return null; // Retorna null em caso de erro.
        }

        // Retorna o conteúdo completo do InputStream convertido em String.
        return stringBuilder.toString();
    }

}