package ifto.aula2.httpgson_executor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    // TextView para exibir os dados da agenda
    private TextView textViewDadosID;

    // URL da API que fornece os dados da agenda em formato JSON (JSON Server)
    private final String URL = "https://my-json-server.typicode.com/Matrixgodo/JSON-TesteAPI/agenda";

    // Serviço de execução de tarefas assíncronas
    private ExecutorService executorService;
    // Handler para executar tarefas na thread principal
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewDadosID = findViewById(R.id.dadosID);

        /**
         * ExecutorService é uma interface do Java que permite gerenciar a execução de tarefas de forma assíncrona,
         * ou seja, tarefas que rodam em segundo plano sem travar a interface do usuário (UI).
         *
         * Neste caso, usamos o método Executors.newSingleThreadExecutor() para criar um serviço que usa apenas **uma única thread**.
         *
         * O que é uma thread?
         * └── Uma thread é como uma linha de execução. Pense em um programa como uma fábrica: a thread é um trabalhador.
         *     Se você tem uma thread, tem um trabalhador; se tiver várias threads, tem vários trabalhadores fazendo tarefas ao mesmo tempo.
         *
         * O que o "single thread executor" faz?
         * └── Ele cria **um único trabalhador** (uma thread) que executa todas as tarefas **uma por uma**, na ordem em que são recebidas.
         *     Isso garante que duas tarefas não rodem ao mesmo tempo, evitando conflitos ou erros causados por acessos simultâneos.
         *
         * Por que isso é útil no Android?
         * └── No Android, não podemos fazer tarefas demoradas (como baixar arquivos da internet) na thread principal, que é responsável pela tela.
         *     Se fizermos isso, o app pode travar ou parar de responder.
         *     Com o ExecutorService, podemos mover essas tarefas para uma thread separada, mantendo o app fluido e responsivo.
         *
         * executorService é a variável que armazena esse serviço. Depois, podemos usar ela para executar qualquer código
         * que queremos que rode em segundo plano (fora da tela principal).
         */
        executorService = Executors.newSingleThreadExecutor();
        /**
         * Handler é uma classe do Android usada para **enviar e processar mensagens ou tarefas (Runnables)**
         * em uma determinada thread — geralmente a **thread principal (UI Thread)**, que é responsável por atualizar a interface do usuário.
         *
         * Por que isso é necessário?
         * └── Porque no Android, **apenas a thread principal pode modificar elementos da interface (tela)**.
         *     Se uma tarefa for executada em segundo plano (por exemplo, por um ExecutorService),
         *     e você tentar atualizar um botão ou um TextView direto daquela thread, o app pode travar ou lançar erro.
         *
         * Looper.getMainLooper() retorna o "laço" (looper) da thread principal.
         * └── Esse "looper" é como uma fila de tarefas que a thread principal fica escutando e executando.
         *
         * Ao criar o Handler com `new Handler(Looper.getMainLooper())`, estamos dizendo:
         * └── "Quero criar um Handler que envie tarefas para serem executadas **na thread principal**".
         *
         * Então, sempre que quisermos executar um trecho de código na tela (por exemplo, mostrar uma mensagem, atualizar um botão),
         * e estivermos **fora da thread principal**, usamos esse handler para "postar" (enviar) a tarefa de volta para ela.
         *
         * Exemplo de uso:
         * ┌──────────────────────────────────────────────┐
         * │ handler.post(() -> meuTextView.setText("OK")); │
         * └──────────────────────────────────────────────┘
         * Isso garante que a alteração do TextView aconteça de forma segura e correta na UI Thread.
         *
         * Resumo:
         * - Handler = ferramenta que permite agendar ou executar código numa thread específica;
         * - Looper.getMainLooper() = pega o "laço de execução" da thread principal (a da interface);
         * - `new Handler(Looper.getMainLooper())` = cria um Handler que permite executar código na interface do usuário (UI Thread).
         */
        handler = new Handler(Looper.getMainLooper());


        // Inicia o download da agenda
        iniciarDownloadDaAgenda();
    }

    private void iniciarDownloadDaAgenda() {
        // mengando um toast para informar que o download está iniciando
        Toast.makeText(this, "Iniciando download...", Toast.LENGTH_SHORT).show();

        /**
         * executorService.execute() é o método responsável por **executar uma tarefa em segundo plano**
         * (fora da thread principal, também chamada de UI Thread).
         *
         * Por que isso é importante?
         * └── No Android, a interface do usuário (botões, textos, imagens, etc.) roda na UI Thread.
         *     Se você fizer tarefas pesadas como **acessar a internet**, **ler arquivos**, ou **processar dados**
         *     diretamente na UI Thread, o aplicativo pode travar ou apresentar lentidão.
         *
         * Esse método recebe um objeto do tipo `Runnable`.
         * └── `Runnable` é uma **interface funcional** do Java, usada para representar uma **tarefa que pode ser executada**.
         * └── Pense em `Runnable` como um "plano de ação" que descreve o que deve ser feito, mas **não executa imediatamente**.
         *     Quem vai decidir quando e onde isso será executado é o `ExecutorService`.
         *
         * Quando usamos:
         * ┌────────────────────────────────────────────┐
         * │ executorService.execute(new Runnable() {   │
         * │     @Override                              │
         * │     public void run() {                    │
         * │         // tarefa que será executada       │
         * │         // em segundo plano                │
         * │     }                                      │
         * │ });                                        │
         * └────────────────────────────────────────────┘
         * Estamos dizendo:
         * "Executor, execute essa tarefa (código dentro do run()) em uma thread separada."
         *
         * Isso é perfeito para:
         * - Fazer chamadas de rede (como consumir uma API);
         * - Ler ou gravar arquivos;
         * - Processar dados pesados sem travar a tela;
         * - Qualquer operação que **não possa ou não deve** ser feita na UI Thread.
         *
         * Resumo:
         * - `Runnable` = um objeto que contém o código a ser executado;
         * - `executorService.execute()` = manda esse código para rodar em segundo plano;
         * - Tudo que estiver dentro do `run()` será executado de forma assíncrona, ou seja, **sem travar o app**.
         */
        executorService.execute(new Runnable() {

            // Método run() é onde colocamos o código que será executado em segundo plano ( fora da thread principal )
            @Override
            public void run() {
                // Fazendo a conexão com a API e obtendo a resposta em formato JSON
                Conexao conexao = new Conexao();
                /**
                 * `obterRespostaHTTP()` é um método da classe `Conexao` que realiza uma **requisição HTTP do tipo GET**
                 * para a URL fornecida. Ele retorna um **InputStream**, que é usado para **ler os dados** recebidos
                 * como resposta do servidor.
                 *
                 * ✳️ O que é um `InputStream`?
                 * - `InputStream` é uma **classe base do Java** usada para **ler dados de forma sequencial** (byte por byte),
                 *   a partir de uma **fonte de entrada**. Essa fonte pode ser um arquivo, uma conexão de rede, ou outro tipo de fluxo.
                 * - Neste caso, a fonte é a **resposta da conexão HTTP**, ou seja, o conteúdo que veio do servidor.
                 *
                 * 📦 Qual é o formato do `InputStream`?
                 * - O `InputStream` em si **não tem um formato definido** (como JSON, XML, texto, imagem...).
                 * - Ele apenas entrega os dados **crus**, em forma de **bytes**, que precisam ser interpretados depois.
                 * - Para saber o "formato real", você precisa verificar o que o servidor retorna.
                 *   Ex: se a API responder com JSON, então esse `InputStream` conterá JSON em formato textual.
                 *
                 *  Para que serve?
                 * - Serve para **ler a resposta da API** ou de qualquer recurso da web.
                 * - Com ele, você pode converter os dados em:
                 *     - `String` (para exibir ou fazer parse, por exemplo com Gson);
                 *     - Arquivo (para salvar localmente);
                 *     - Outro tipo de dado, dependendo do uso.
                 *
                 *  Como usar?
                 * Normalmente, convertemos um `InputStream` para `String` para facilitar o uso, assim:
                 *
                 *     InputStream inputStream = conexao.obterRespostaHTTP(URL);
                 *     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                 *     StringBuilder resultado = new StringBuilder();
                 *     String linha;
                 *     while ((linha = reader.readLine()) != null) {
                 *         resultado.append(linha);
                 *     }
                 *     reader.close();
                 *     String respostaJson = resultado.toString();
                 *
                 * A partir daí, você pode fazer o que quiser com essa `respostaJson`:
                 * - Exibir em tela,
                 * - Fazer o parse com Gson,
                 * - Armazenar,
                 * - Etc.
                 *
                 * Resumo:
                 * - `InputStream` é como uma "mangueira" por onde os dados chegam.
                 * - Ele não define o formato, apenas **transfere os dados byte a byte**.
                 * - Você precisa converter esse fluxo em algo utilizável (como `String`).
                 */
                InputStream inputStream = conexao.obterRespostaHTTP(URL);

                // Convertendo o InputStream para String
                String resposta = conexao.converter(inputStream);

                
                Log.i("JSON", resposta);

                if (resposta != null) {
                    Gson gson = new Gson();
                    // Corrigindo a desserialização para lista de objetos
                    Type type = new TypeToken<List<Agenda>>() {}.getType();
                    List<Agenda> listaAgenda = gson.fromJson(resposta, type);

                    // Construindo a String a ser exibida
                    StringBuilder builder = new StringBuilder();
                    for (Agenda agenda : listaAgenda) {
                        builder.append(agenda.toString()).append("\n");
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing() && !isDestroyed()) {
                                textViewDadosID.setText(builder.toString());
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Erro ao baixar dados JSON", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }
}
