package com.diarioescolar.repositorio;

import android.util.Log;
import android.widget.Toast;

import com.diarioescolar.Conexao;
import com.diarioescolar.modelo.Estudante;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório responsável por acessar os dados de estudantes através de uma API REST.
 * Usa a classe Conexao para realizar requisições HTTP (GET, POST, PUT, DELETE).
 */
public class EstudanteRepositorio {

    // URL base da API de estudantes. Pode ser movida para uma configuração externa futuramente.
    private final String BASE_URL = "https://192.168.1.14:8080/estudantes/"; // Tornar BASE_URL configurável (ex: via SharedPreferences, BuildConfig, .properties, ou variável de ambiente).

    // Instância da classe responsável por lidar com conexões HTTP.
    private final Conexao conexao;

    /**
     * Construtor que inicializa o repositório com uma nova instância de Conexao.
     */
    public EstudanteRepositorio() {
        this.conexao = new Conexao();
    }

    /**
     * Realiza uma requisição GET à API para buscar uma lista de estudantes com campos selecionados
     * (por exemplo: ID, nome e idade), convertendo a resposta JSON em uma lista de objetos `Estudante`.
     *
     * @return Lista de estudantes convertida a partir da resposta JSON.
     * @throws RuntimeException em caso de falha na conexão ou no processamento do JSON.
     */
    public List<Estudante> buscarListaEstudantesIdNomeIdadeTeste() {
        try {
            // Realiza requisição HTTP GET
            InputStream ipStream = conexao.get(BASE_URL);

            // Converte o InputStream da resposta para texto JSON
            String textoJSON = conexao.converterParaTexto(ipStream);

            // Converte o JSON em uma lista de objetos Estudante
            Gson gson = new Gson();
            return gson.fromJson(textoJSON, new TypeToken<List<Estudante>>(){}.getType());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar a lista de estudantes: " + e.getMessage(), e);
        }
    }

    /**
     * Realiza uma requisição HTTP GET para obter uma lista de estudantes a partir da API.
     * Utiliza leitura em stream via JsonReader para processar os dados com maior eficiência de memória,
     * ideal para grandes volumes de dados (ex: 900 mil estudantes).
     *
     * @return Lista de objetos Estudante representando os dados recebidos da API.
     * @throws RuntimeException caso ocorra erro de conexão, leitura ou conversão do JSON.
     */
    public List<Estudante> buscarListaEstudantesIdNomeIdade() {
        try (
                // Realiza a conexão e obtém o fluxo de resposta da API
                InputStream ipStream = conexao.get(BASE_URL);

                // Converte o fluxo de bytes em caracteres
                InputStreamReader reader = new InputStreamReader(ipStream);

                // Usa JsonReader para processar o JSON de forma eficiente (streaming)
                JsonReader jsonReader = new JsonReader(reader)
        ) {
            // Instancia do Gson para deserializar os dados JSON
            Gson gson = new Gson();

            // Converte o JSON lido para uma lista de objetos Estudante
            return gson.fromJson(jsonReader, new TypeToken<List<Estudante>>() {}.getType());

        } catch (Exception e) {
            // Em caso de qualquer falha, encapsula e relança uma exceção com mensagem detalhada
            throw new RuntimeException("Erro ao buscar a lista de estudantes: " + e.getMessage(), e);
        }
    }



    /**
     * Realiza uma requisição HTTP GET para obter os dados de um estudante específico pelo seu ID.
     *
     * @param id Identificador único do estudante.
     * @return Objeto Estudante correspondente ao ID fornecido.
     * @throws RuntimeException em caso de falha na conexão, leitura ou conversão do JSON.
     */
    public Estudante buscarEstudantePorIdTeste(int id) {
        try {
            // Monta a URL completa incluindo o ID do estudante
            String url = BASE_URL + id;

            // Realiza a requisição GET e obtém a resposta como InputStream
            InputStream ipStream = conexao.get(url);

            // Converte o fluxo de bytes em uma String contendo o JSON da resposta
            String textoJSON = conexao.converterParaTexto(ipStream);

            // Converte o JSON para um objeto Estudante utilizando a biblioteca Gson
            Gson gson = new Gson();
            return gson.fromJson(textoJSON, Estudante.class);

        } catch (Exception e) {
            // Em caso de erro, encapsula e relança a exceção com detalhes
            throw new RuntimeException("Erro ao buscar estudante por ID: " + id, e);
        }
    }

    /**
     * Busca um estudante específico pelo seu ID via requisição HTTP GET.
     *
     * O método realiza a conexão com a URL fornecida, lê a resposta JSON diretamente do fluxo
     * sem armazená-la como String (economizando memória), e a converte em um objeto Estudante.
     *
     * @param id O identificador único do estudante que será consultado.
     * @return Um objeto Estudante correspondente ao ID fornecido.
     * @throws RuntimeException Em caso de falha na conexão, leitura ou conversão do JSON.
     */
    public Estudante buscarEstudantePorId(int id) {
        try (
                // Abre o fluxo de entrada da requisição GET para a URL do estudante
                InputStream ipStream = conexao.get(BASE_URL + id);

                // Converte o fluxo de bytes para caracteres
                InputStreamReader reader = new InputStreamReader(ipStream);

                // Cria um leitor de JSON eficiente que lê diretamente do fluxo (sem converter para String)
                JsonReader jsonReader = new JsonReader(reader)
        ) {
            // Instancia do Gson para converter o JSON em objeto Estudante
            Gson gson = new Gson();

            // Faz o parsing do JSON para o objeto Estudante e o retorna
            return gson.fromJson(jsonReader, Estudante.class);

        } catch (Exception e) {
            // Captura qualquer erro durante a operação e encapsula em uma RuntimeException
            throw new RuntimeException("Erro ao buscar estudante por ID: " + id, e);
        }
    }



    /**
     * Exclui um estudante no servidor com base no seu ID.
     *
     * Este método monta a URL do recurso do estudante a partir do ID fornecido
     * e realiza uma requisição HTTP DELETE usando a classe Conexao.
     *
     * @param id O identificador único do estudante a ser removido.
     * @throws RuntimeException Se ocorrer qualquer erro durante a exclusão.
     */
    public void excluirEstudantePorId(int id) {
        try {
            // Constrói a URL do recurso a ser excluído
            String url = BASE_URL + id;

            // Realiza a requisição HTTP DELETE para a URL do estudante
            boolean sucesso = conexao.excluirEstudante(url);

            if (!sucesso) {
                Log.e("ExcluirEstudante", "Falha ao excluir estudante com ID: " + id);
                return;
            }

        } catch (Exception e) {
            // Encapsula qualquer erro ocorrido em uma exceção genérica de tempo de execução
            throw new RuntimeException("Erro ao excluir estudante com ID: " + id, e);
        }
    }


    /**
     * Busca estudantes com os dados completos a partir de uma lista com dados parciais.
     *
     * Este método percorre cada estudante da lista fornecida (que contém apenas dados parciais,
     * como ID, nome e idade) e realiza uma requisição HTTP GET para buscar os dados completos
     * de cada um, adicionando-os a uma nova lista que é retornada no final.
     *
     * @param estudanteList Lista de estudantes com informações parciais (ex: ID, nome, idade).
     * @return Lista de estudantes com todos os dados completos. Retorna uma lista vazia se a entrada for nula ou vazia.
     */
    public List<Estudante> buscarListaEstudantesComDadosCompletosTeste(List<Estudante> estudanteList) {
        // Cria a lista onde serão armazenados os estudantes com dados completos
        List<Estudante> listaCompleta = new ArrayList<>();

        // Verifica se a lista recebida é nula ou vazia
        if (estudanteList == null || estudanteList.isEmpty()) {
            return listaCompleta; // Retorna lista vazia, evitando processamento desnecessário
        }

        // Instância do Gson para conversão JSON -> Objeto Java
        Gson gson = new Gson();

        // Percorre cada estudante da lista fornecida
        for (Estudante estudante : estudanteList) {
            // Monta a URL para buscar o estudante completo com base no ID
            String url = BASE_URL + estudante.getId();

            // Realiza a requisição HTTP GET e trata a resposta
            try (InputStream respostaStream = conexao.get(url)) {
                // Converte a resposta do InputStream para texto JSON
                String jsonRetornado = conexao.converterParaTexto(respostaStream);

                // Desserializa o JSON para um objeto Estudante completo
                Estudante estudanteCompleto = gson.fromJson(jsonRetornado, Estudante.class);

                // Adiciona o estudante com dados completos na lista final
                listaCompleta.add(estudanteCompleto);

            } catch (Exception e) {
                // Registra o erro com detalhes, mas continua o processamento dos demais estudantes
                System.err.println("Erro ao buscar dados do estudante ID " + estudante.getId() + ": " + e.getMessage());
                e.printStackTrace(); // ou use um logger profissional como SLF4J/Log4j
            }
        }

        // Retorna a lista preenchida com os dados completos dos estudantes
        return listaCompleta;
    }


    /**
     * Busca os dados completos de cada estudante presente na lista fornecida.
     *
     * Para cada estudante da lista (que pode conter apenas dados parciais como ID, nome, idade),
     * este método realiza uma requisição HTTP individual (GET) para buscar os dados completos no servidor.
     *
     * @param estudanteList Lista de estudantes com dados parciais (ex: ID, nome, idade).
     * @return Lista contendo os estudantes com todos os dados completos. Se a lista de entrada for nula ou vazia, retorna uma lista vazia.
     */
    public List<Estudante> buscarListaEstudantesComDadosCompletos(List<Estudante> estudanteList) {
        List<Estudante> listaCompleta = new ArrayList<>();

        // Evita NPE e chamadas desnecessárias
        if (estudanteList == null || estudanteList.isEmpty()) {
            return listaCompleta;
        }

        Gson gson = new Gson();

        for (Estudante estudante : estudanteList) {
            String url = BASE_URL + estudante.getId();

            try (
                    InputStream respostaStream = conexao.get(url);
                    InputStreamReader reader = new InputStreamReader(respostaStream);
                    JsonReader jsonReader = new JsonReader(reader)
            ) {
                // Desserializa o JSON para um objeto Estudante completo
                Estudante estudanteCompleto = gson.fromJson(jsonReader, Estudante.class);
                listaCompleta.add(estudanteCompleto);

            } catch (Exception e) {
                // Registra o erro e continua processando os demais estudantes
                System.err.println("Erro ao buscar dados completos do estudante ID " + estudante.getId() + ": " + e.getMessage());
                e.printStackTrace(); // Em produção, use um logger profissional como SLF4J ou Log4j
            }
        }

        return listaCompleta;
    }


    /**
     * Atualiza os dados de um estudante no servidor com base no seu ID.
     *
     * Este método realiza uma requisição HTTP PUT para o endpoint da API correspondente
     * ao estudante com o ID especificado, enviando o objeto {@link Estudante} com os dados
     * atualizados em formato JSON. A resposta do servidor, também em JSON, é desserializada
     * e retornada como um novo objeto {@link Estudante}, refletindo o estado final após a atualização.
     *
     * @param id O identificador único do estudante que será atualizado.
     * @param estudanteAtualizado Um objeto {@link Estudante} com os novos dados a serem enviados.
     * @return Um objeto {@link Estudante} atualizado, conforme retornado pela API.
     * @throws RuntimeException Se ocorrer qualquer erro de conexão, comunicação ou processamento.
     */
    public Estudante atualizarEstudanteTeste(int id, Estudante estudanteAtualizado) {
        try {
            // Monta a URL completa para atualizar o estudante com o ID fornecido
            String url = BASE_URL + id;

            // Envia a requisição PUT com o estudante atualizado como corpo da requisição
            InputStream respostaStream = conexao.enviarEstudanteViaPut(url, estudanteAtualizado);

            // Converte o fluxo de resposta do servidor (JSON) em texto legível
            String respostaJSON = conexao.converterParaTexto(respostaStream);

            // Cria uma instância do Gson para realizar a desserialização
            Gson gson = new Gson();

            // Converte o JSON da resposta em um objeto Estudante e o retorna
            return gson.fromJson(respostaJSON, Estudante.class);

        } catch (Exception e) {
            // Trata exceções genéricas e fornece uma mensagem clara com o ID do estudante afetado
            throw new RuntimeException("Erro ao atualizar estudante com ID: " + id, e);
        }
    }

    /**
     * Atualiza os dados de um estudante no servidor com base no seu ID.
     *
     * Este método realiza uma requisição HTTP PUT para o endpoint da API correspondente
     * ao estudante com o ID especificado, enviando o objeto {@link Estudante} com os dados
     * atualizados no corpo da requisição em formato JSON.
     *
     * A resposta do servidor, também em JSON, é desserializada e retornada como um novo objeto
     * {@link Estudante}, refletindo o estado final após a atualização.
     *
     * @param id O identificador único do estudante que será atualizado.
     * @param estudanteAtualizado Um objeto {@link Estudante} com os dados atualizados.
     * @return Um objeto {@link Estudante} atualizado, conforme retornado pela API.
     * @throws RuntimeException Se ocorrer qualquer erro de conexão, comunicação ou processamento.
     */
    public Estudante atualizarEstudante(int id, Estudante estudanteAtualizado) {
        try {
            // Constrói a URL do recurso do estudante a ser atualizado
            String url = BASE_URL + id;

            // Envia a requisição PUT com o objeto Estudante como corpo JSON
            try (
                    InputStream respostaStream = conexao.enviarEstudanteViaPut(url, estudanteAtualizado);
                    InputStreamReader reader = new InputStreamReader(respostaStream);
                    JsonReader jsonReader = new JsonReader(reader)
            ) {
                // Desserializa o JSON de resposta para um objeto Estudante
                Gson gson = new Gson();
                return gson.fromJson(jsonReader, Estudante.class);
            }

        } catch (Exception e) {
            // Relata claramente o erro, incluindo o ID do estudante
            throw new RuntimeException("Erro ao atualizar estudante com ID: " + id, e);
        }
    }

    /**
     * Cadastra um novo estudante no servidor.
     *
     * Este método converte o objeto {@link Estudante} em JSON e envia via
     * requisição HTTP POST para o endpoint da API.
     *
     * @param e Objeto {@link Estudante} a ser cadastrado; se for nulo, o método não executa nada.
     * @throws RuntimeException Se a requisição falhar ou o estudante for nulo.
     */
    public void cadastrarEstudante(Estudante e) {
        if (e == null) {
            throw new IllegalArgumentException("O estudante não pode ser nulo.");
        }

        // Converte o objeto Estudante em JSON usando Gson
        Gson gson = new Gson();
        String jsonEstudante = gson.toJson(e);

        // Tenta cadastrar o estudante na API via POST
        boolean sucesso = conexao.cadastrarEstudante(BASE_URL, jsonEstudante);

        // Loga o JSON enviado em caso de sucesso
        if (sucesso) {
            Log.d("JSON_ENVIADO", jsonEstudante);
        } else {
            // Lança exceção caso a requisição não tenha sido bem sucedida
            throw new RuntimeException("Falha ao cadastrar estudante no servidor.");
        }
    }
}
