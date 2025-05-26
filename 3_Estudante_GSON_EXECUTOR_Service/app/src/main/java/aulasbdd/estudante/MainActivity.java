package aulasbdd.estudante;

import static aulasbdd.estudante.Calculo_Aluno.*;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private static final String TAG = "MyActivity";
    private String URL = "https://my-json-server.typicode.com/Matrixgodo/JSON-TesteAPI-Estudante/db";
    private ExecutorService executorService;
    private Handler handler;
    private List<Estudante> listaEstudantes;
    private ListView listaDeVisualizacao;

    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaDeVisualizacao = findViewById(R.id.listView);
        listaDeVisualizacao.setOnItemClickListener(this);
        // criando uma thread para executar a tarefa de obter os dados da API em segundo plano (assíncrona)
        executorService = Executors.newSingleThreadExecutor();
        // criando um handler para atualizar a interface do usuário na thread principal (UI Thread) e gerenciar mensagens e tarefas assíncronas
        handler = new Handler();
        iniciarDowload();

    }

    private void iniciarDowload() {
        Toast.makeText(this, "Iniciando download...", Toast.LENGTH_SHORT).show();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Conexao conexao = new Conexao();
                InputStream inputStream = conexao.obterRespostaHTTP(URL);
                String json = conexao.converter(inputStream);

                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Erro ao obter dados", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                Gson gson = new Gson();
                Plain_Old_Java_Object listaEstudantesObj = gson.fromJson(json,new TypeToken<Plain_Old_Java_Object>() {});
                listaEstudantes = listaEstudantesObj.getEstudantes();
                handler.post(new Runnable() { // atualizando a interface do usuário na thread principal (UI Thread)
                    @Override
                    public void run() {
                        items = new ArrayList<>();
                        for (Estudante a : listaEstudantes) {
                            items.add(a.getNome());
                        }
                        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                        listaDeVisualizacao.setAdapter(adapter);
                        // Registro de log para verificar se a lista foi populada corretamente
                        Log.d(TAG, "Completou o download: " + listaEstudantes.toString()); // Enviar uma DEBUG mensagem de log.
                        Toast.makeText(MainActivity.this, "Download concluído", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listaEstudantes == null || position >= listaEstudantes.size()) return;

        Estudante estudanteSelecionado = listaEstudantes.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Detalhes do Estudante");
        builder.setMessage("Nome: " + estudanteSelecionado.getNome() +
                "\nIdade: " + estudanteSelecionado.getIdade() +
                "\nNotas: " + removerColchete(estudanteSelecionado.getNotas().toString()) +
                "\nPresença: " + removerColchete(estudanteSelecionado.getPresenca().toString()) );
        builder.setPositiveButton("ESTATISTICA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                trocarDeTela(MainActivity.this, SegundatTela.class,estudanteSelecionado);
            }
        });
        builder.setNeutralButton("ESTATISTICA GERAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                trocarDeTelaTerceira(MainActivity.this, EstatiticaGeral_Activity.class, listaEstudantes);
            }
        });
        builder.show();

        Toast.makeText(MainActivity.this, "Detalhes do estudante selecionado", Toast.LENGTH_SHORT).show();
        Log.d("MainActivity", "Detalhes do estudante selecionado: " + estudanteSelecionado.toString());
    }

    public String removerColchete(String palavra) {
        // substitui uma expressao por outra
        return palavra.replaceAll("[\\[\\]]", "");
    }

    public void trocarDeTela(AppCompatActivity activity, Class classe_OndeQueroIr, Estudante estudanteSelecionado){
        Intent it = new Intent(activity,classe_OndeQueroIr);
        Log.d("MainActivity", "Detalhes do estudante selecionado: ");

        double nota = notaFinal(estudanteSelecionado.getNotas());
        int porcentagem = porcentagemPresenca(estudanteSelecionado.getPresenca());
        String aprovacao = situacaoDeAprovacao(estudanteSelecionado.getPresenca(),estudanteSelecionado.getNotas());
        // armazenando dados na Intent
        it.putExtra("nota",nota);
        it.putExtra("porcentagem", porcentagem);
        it.putExtra("aprovacao", aprovacao);
        startActivity(it);
    }

    public void trocarDeTelaTerceira(AppCompatActivity activity, Class classe_OndeQueroIr, List<Estudante> listaEstudantes) {
        Intent it = new Intent(activity, classe_OndeQueroIr);

        // Usando a classe Calculo_Aluno para fazer os cálculos com a lista de estudantes
        double mediaNotaGeral = Calculo_Aluno.mediaNotaGeral(listaEstudantes);
        String situacao = Calculo_Aluno.situacaoDeAprovacao(listaEstudantes.get(0).getPresenca(), listaEstudantes.get(0).getNotas()); // Exemplo para pegar o primeiro aluno
        String nomeMaiorNota = Calculo_Aluno.maiorNota(listaEstudantes);
        String nomeMenorNota = Calculo_Aluno.menorNota(listaEstudantes);
        double mediaIdade = Calculo_Aluno.mediaIdade(listaEstudantes);

        // Passando os dados calculados pela Intent
        it.putExtra("mediaNotaGeral", mediaNotaGeral);
        it.putExtra("situacao", situacao);
        it.putExtra("nomeMaiorNota", nomeMaiorNota);
        it.putExtra("nomeMenorNota", nomeMenorNota);
        it.putExtra("mediaIdade", mediaIdade);

        // Passando listas de aprovados e reprovados (transformados em strings)
        String aprovados = String.join(", ", Calculo_Aluno.aprovados(listaEstudantes));
        String reprovados = String.join(", ", Calculo_Aluno.reprovados(listaEstudantes));

        it.putExtra("aprovados", aprovados);
        it.putExtra("reprovados", reprovados);

        // Inicia a nova Activity
        activity.startActivity(it);
    }



}