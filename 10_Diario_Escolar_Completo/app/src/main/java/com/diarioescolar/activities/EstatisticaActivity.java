package com.diarioescolar.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diarioescolar.R;
import com.diarioescolar.adapter.AprovadosAdapter;
import com.diarioescolar.adapter.ReprovadosAdapter;
import com.diarioescolar.modelo.Estudante;
import com.diarioescolar.modelo.Turma;
import com.diarioescolar.viewmodel.EstudanteViewModel;
import com.diarioescolar.viewmodel.TurmaViewModel;

import java.util.ArrayList;
import java.util.List;

public class EstatisticaActivity extends AppCompatActivity {

    private TextView mediaGeralText;
    private TextView maiorNotaText;
    private TextView menorNotaText;
    private TextView mediaIdadeText;
    private RecyclerView aprovadosList;
    private RecyclerView reprovadosList;
    private TurmaViewModel turmaViewModel;
    private EstudanteViewModel estudanteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatistica);
        initViews();

        turmaViewModel = new ViewModelProvider(this).get(TurmaViewModel.class);
        estudanteViewModel = new ViewModelProvider(this).get(EstudanteViewModel.class);

        estudanteViewModel.buscarListaEstudantes();

        estudanteViewModel.getListEstudanteLiveData().observe(this, estudanteList -> {
            turmaViewModel.carregarListaDeAlunos(estudanteList);
        });

        turmaViewModel.getListEstudantesLiveData().observe(this, estudanteList -> {
            turmaViewModel.carregarDadosTurma(estudanteList);
        });

        turmaViewModel.getTurmaLiveData().observe(this, turma -> {
            preenchendo(turma);
        });
    }

    private void initViews() {
        mediaGeralText = findViewById(R.id.media_geral);
        maiorNotaText = findViewById(R.id.maior_nota);
        menorNotaText = findViewById(R.id.menor_nota);
        mediaIdadeText = findViewById(R.id.media_idade);
        aprovadosList = findViewById(R.id.aprovados_list);
        reprovadosList = findViewById(R.id.reprovados_list);
    }

    private void preenchendo(Turma turma) {
        mediaGeralText.setText("Média Geral: " + String.format("%.1f", turma.getMediaGeralNotas()));
        maiorNotaText.setText("Maior Nota: " + turma.getNomeAlunoMaiorNota());
        menorNotaText.setText("Menor Nota: " + turma.getNomeAlunoMenorNota());
        mediaIdadeText.setText("Média Idade: " + String.format("%.1f", turma.getMediaIdade()));

        // RecyclerView de aprovados e desaprovados
        AprovadosAdapter aprovadosAdapter = new AprovadosAdapter(turma.getNomesAprovados());
        aprovadosList.setAdapter(aprovadosAdapter);
        aprovadosList.setLayoutManager(new LinearLayoutManager(this));

        ReprovadosAdapter reprovadosAdapter = new ReprovadosAdapter(turma.getNomesReprovados());
        reprovadosList.setAdapter(reprovadosAdapter);
        reprovadosList.setLayoutManager(new LinearLayoutManager(this));

    }

}
