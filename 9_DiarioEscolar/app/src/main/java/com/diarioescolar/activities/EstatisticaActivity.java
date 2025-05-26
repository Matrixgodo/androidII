package com.diarioescolar.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.diarioescolar.R;
import com.diarioescolar.Calcular_DadosAlunos;
import com.diarioescolar.modelo.EstudanteModel;
import com.diarioescolar.viewmodel.EstudanteViewModel;

import java.util.ArrayList;
import java.util.List;

public class EstatisticaActivity extends AppCompatActivity {

    private static final String TAG = "EstatisticaActivity";
    private TextView mediaGeralText;
    private TextView maiorNotaText;
    private TextView menorNotaText;
    private TextView mediaIdadeText;
    private ListView aprovadosList;
    private ListView reprovadosList;
    private EstudanteViewModel estudanteViewModel;
    private List<EstudanteModel> estudanteList = new ArrayList<>();
    ArrayAdapter<String> aprovadosAdapter;
    ArrayAdapter<String> reprovadosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatistica);
        initViews();


        estudanteViewModel = new ViewModelProvider(this).get(EstudanteViewModel.class);
        estudanteViewModel.getEstudanteLiveData().observe(this, estudantes -> {
            if (estudantes != null) {
                estudanteList.clear();
                estudanteList.addAll(estudantes);
                //preenchendo();
            } else {
                Toast.makeText(this, "Erro ao carregar estudantes", Toast.LENGTH_SHORT).show();
            }
        });
        estudanteViewModel.buscarListaEstudantes();
    }

    private void initViews() {
        mediaGeralText = findViewById(R.id.media_geral);
        maiorNotaText = findViewById(R.id.maior_nota);
        menorNotaText = findViewById(R.id.menor_nota);
        mediaIdadeText = findViewById(R.id.media_idade);
        aprovadosList = findViewById(R.id.aprovados_list);
        reprovadosList = findViewById(R.id.reprovados_list);
    }

    private void preenchendo() {

        double mediaGeral = Calcular_DadosAlunos.calcularMediaGeralTurma(estudanteList);
        mediaGeralText.setText("Média Geral: " + String.format("%.1f", mediaGeral));

        // Nome do aluno com maior nota
        String maiorNota = Calcular_DadosAlunos.getNomeAlunoMaiorNota(estudanteList);
        maiorNotaText.setText("Maior Nota: " + maiorNota);

        // Nome do aluno com menor nota
        String menorNota = Calcular_DadosAlunos.getNomeAlunoMenorNota(estudanteList);
        menorNotaText.setText("Menor Nota: " + menorNota);

        // Média de idade da turma
        double mediaIdade = Calcular_DadosAlunos.calcularMediaIdadeTurma(estudanteList);
        mediaIdadeText.setText("Média Idade: " + String.format("%.1f", mediaIdade));

        // Lista de aprovados e reprovados
        Calcular_DadosAlunos.AlunosPorSituacao situacao = Calcular_DadosAlunos.getAlunosPorSituacao(estudanteList);
        List<String> nomesAprovados = new ArrayList<>();
        for (EstudanteModel estudante : situacao.getAprovados()) {
            nomesAprovados.add(estudante.getNome());
        }
        List<String> nomesReprovados = new ArrayList<>();
        for (EstudanteModel estudante : situacao.getReprovados()) {
            nomesReprovados.add(estudante.getNome());
        }

        aprovadosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nomesAprovados);
        reprovadosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nomesReprovados);

        aprovadosList.setAdapter(aprovadosAdapter);
        reprovadosList.setAdapter(reprovadosAdapter);
    }


}