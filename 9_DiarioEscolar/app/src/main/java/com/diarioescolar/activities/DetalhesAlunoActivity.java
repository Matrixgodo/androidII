package com.diarioescolar.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diarioescolar.Calcular_DadosAlunos;
import com.diarioescolar.R;
import com.diarioescolar.adapter.FrequenciaAdapter;
import com.diarioescolar.adapter.NotasAdapter;
import com.diarioescolar.modelo.EstudanteModel;
import com.diarioescolar.viewmodel.EstudanteViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class DetalhesAlunoActivity extends AppCompatActivity {
    private EstudanteViewModel estudanteViewModel;

    private TextInputLayout inputNome;
    private TextInputLayout inputIdade;
    private TextInputLayout inputStatus, inputPresenca_Percentual, inputNota_Final;

    private RecyclerView recyclerListaPresenca1;
    private RecyclerView recyclerListaNotas;

    // Meus adaptadores
    NotasAdapter notasAdapter;
    FrequenciaAdapter frequenciaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_aluno);
        estudanteViewModel = new ViewModelProvider(this).get(EstudanteViewModel.class);
        initViews();

        estudanteViewModel.getEstudanteByIdLiveData().observe(this, estudanteModel -> {
            if (estudanteModel != null) {
                preenchendoasViews(estudanteModel);
            } else {
                Toast.makeText(this, "Erro ao carregar dados do estudante", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        inputNome = findViewById(R.id.nome);
        inputIdade = findViewById(R.id.idade);
        inputStatus = findViewById(R.id.status);
        inputPresenca_Percentual = findViewById(R.id.presenca_percentual);
        inputNota_Final = findViewById(R.id.nota_final);

        recyclerListaPresenca1 = findViewById(R.id.recyclerListaPresenca1);
        recyclerListaNotas = findViewById(R.id.recyclerListaNotas);

        recyclerListaPresenca1.setLayoutManager(new LinearLayoutManager(this));
        recyclerListaNotas.setLayoutManager(new LinearLayoutManager(this));

        int idEstudante = getIntent().getIntExtra("id_estudante", -1);
        estudanteViewModel.buscarEstudantePorId(idEstudante);
    }

    private void preenchendoasViews(EstudanteModel estudanteModel) {
        // nome
        inputNome.getEditText().setText(estudanteModel.getNome());
        inputIdade.getEditText().setText(String.valueOf(estudanteModel.getIdade()));

        //  nota final
        double notaFinal = Calcular_DadosAlunos.calcularNotaFinal(estudanteModel);
        inputNota_Final.getEditText().setText(String.format("%.1f", notaFinal));

        //  percentual da presenca
        double percentualPresenca = Calcular_DadosAlunos.calcularPercentualPresenca(estudanteModel);
        inputPresenca_Percentual.getEditText().setText(String.format("%.1f%%", percentualPresenca));

        // Calcula e exibe a situação final
        String situacaoFinal = Calcular_DadosAlunos.calcularSituacaoFinal(estudanteModel);
        inputStatus.getEditText().setText(situacaoFinal);

        // Configura os RecyclerViews para exibir frequência e notas
        notasAdapter = new NotasAdapter(estudanteModel.getNotas() != null ? estudanteModel.getNotas() : new ArrayList<>());
        frequenciaAdapter = new FrequenciaAdapter(estudanteModel.getPresenca() != null ? estudanteModel.getPresenca() : new ArrayList<>());

        recyclerListaPresenca1.setAdapter(frequenciaAdapter);
        recyclerListaNotas.setAdapter(notasAdapter);
    }
}