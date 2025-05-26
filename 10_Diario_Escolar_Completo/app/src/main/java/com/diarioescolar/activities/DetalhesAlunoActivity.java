package com.diarioescolar.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diarioescolar.utils.Calcular_DadosAlunos;
import com.diarioescolar.R;
import com.diarioescolar.adapter.FrequenciaAdapter;
import com.diarioescolar.adapter.NotasAdapter;
import com.diarioescolar.modelo.Estudante;
import com.diarioescolar.viewmodel.EstudanteViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class DetalhesAlunoActivity extends AppCompatActivity implements View.OnClickListener {
    private EstudanteViewModel estudanteViewModel;

    private TextInputLayout inputNome;
    private TextInputLayout inputIdade;
    private TextInputLayout inputStatus, inputPresenca_Percentual, inputNota_Final;

    private MaterialButton btn_Frequencia, btn_Nota;

    private RecyclerView recyclerListaPresenca1;
    private RecyclerView recyclerListaNotas;

    private Estudante estudante;

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
                estudante = estudanteModel;
            } else {
                Toast.makeText(this, "Erro ao carregar dados do estudante", Toast.LENGTH_SHORT).show();
            }
        });

        btn_Nota.setOnClickListener(this);
        btn_Frequencia.setOnClickListener(this);
    }

    private void initViews() {
        inputNome = findViewById(R.id.nome);
        inputIdade = findViewById(R.id.idade);
        inputStatus = findViewById(R.id.status);
        inputPresenca_Percentual = findViewById(R.id.presenca_percentual);
        inputNota_Final = findViewById(R.id.nota_final);

        btn_Frequencia = findViewById(R.id.btn_frequencia);
        btn_Nota = findViewById(R.id.btn_nota);

        recyclerListaPresenca1 = findViewById(R.id.recyclerListaPresenca1);
        recyclerListaNotas = findViewById(R.id.recyclerListaNotas);

        recyclerListaPresenca1.setLayoutManager(new LinearLayoutManager(this));
        recyclerListaNotas.setLayoutManager(new LinearLayoutManager(this));

        int idEstudante = getIntent().getIntExtra("id_estudante", -1);
        estudanteViewModel.buscarEstudantePorId(idEstudante);
    }

    private void preenchendoasViews(Estudante estudante) {
        // nome
        inputNome.getEditText().setText(estudante.getNome());
        inputIdade.getEditText().setText(String.valueOf(estudante.getIdade()));

        //  nota final
        double notaFinal = Calcular_DadosAlunos.calcularNotaFinal(estudante);
        inputNota_Final.getEditText().setText(String.format("%.1f", notaFinal));

        //  percentual da presenca
        double percentualPresenca = Calcular_DadosAlunos.calcularPercentualPresenca(estudante);
        inputPresenca_Percentual.getEditText().setText(String.format("%.1f%%", percentualPresenca));

        // Calcula e exibe a situação final
        String situacaoFinal = Calcular_DadosAlunos.calcularSituacaoFinal(estudante);
        inputStatus.getEditText().setText(situacaoFinal);

        // Configura os RecyclerViews para exibir frequência e notas
        notasAdapter = new NotasAdapter(estudante.getNotas() != null ? estudante.getNotas() : new ArrayList<>());
        frequenciaAdapter = new FrequenciaAdapter(estudante.getPresenca() != null ? estudante.getPresenca() : new ArrayList<>());

        recyclerListaPresenca1.setAdapter(frequenciaAdapter);
        recyclerListaNotas.setAdapter(notasAdapter);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_nota) {
            View dialogView = getLayoutInflater().inflate(R.layout.adicionar_nota_frequencia, null);

            AlertDialog.Builder construtor = new MaterialAlertDialogBuilder(DetalhesAlunoActivity.this);
            construtor.setView(dialogView);

            AlertDialog dialog = construtor.create();
            dialog.show();

            TextInputEditText inputNota = dialogView.findViewById(R.id.nota_frequencia);
            MaterialButton btnAdd = dialogView.findViewById(R.id.btn_add);
            MaterialButton btnCancelar = dialogView.findViewById(R.id.btnCancelar);

            btnCancelar.setOnClickListener(view -> dialog.dismiss());

            btnAdd.setOnClickListener(view -> {
                String valorNota = inputNota.getText().toString().trim();

                if (!valorNota.isEmpty()) {
                    try {
                        Double nota = Double.parseDouble(valorNota);
                        if (nota >= 0 && nota <= 10) {
                             
                            if (estudante != null) {
                                // Adiciona a nova nota à lista
                                if (estudante.getNotas() == null) {
                                    estudante.setNotas(new ArrayList<>());
                                }
                                estudante.getNotas().add(nota);

                                // Atualiza o estudante
                                estudanteViewModel.atualizarEstudante(estudante);
                                estudanteViewModel.buscarEstudantePorId(estudante.getId());

                                dialog.dismiss();
                                Toast.makeText(DetalhesAlunoActivity.this, "Nota adicionada com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            inputNota.setError("Nota deve estar entre 0 e 10");
                        }
                    } catch (NumberFormatException e) {
                        inputNota.setError("Valor inválido");
                    }
                } else {
                    inputNota.setError("Campo obrigatório");
                }
            });
        } else if (v.getId() == R.id.btn_frequencia) {
            View dialogView = getLayoutInflater().inflate(R.layout.adicionar_frequencia, null);

            AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                    .setView(dialogView)
                    .create();

            Spinner spinnerFrequencia = dialogView.findViewById(R.id.spinner);
            MaterialButton btnConfirmar = dialogView.findViewById(R.id.concluir);
            MaterialButton btnCancelar = dialogView.findViewById(R.id.cancelar);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.frequencia,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFrequencia.setAdapter(adapter);

            btnCancelar.setOnClickListener(view -> dialog.dismiss());

            btnConfirmar.setOnClickListener(view -> {
                String selecao = spinnerFrequencia.getSelectedItem().toString();

                if (estudante != null) {
                    if (estudante.getPresenca() == null) {
                        estudante.setPresenca(new ArrayList<>());
                    }
                    estudante.getPresenca().add(selecao.equals("Presente") ? true : false);
                    // estudante.getPresenca().add(selecao.equals("Presente"));
                    estudanteViewModel.atualizarEstudante(estudante);
                    estudanteViewModel.buscarEstudantePorId(estudante.getId());
                    Toast.makeText(this, "Frequência adicionada: " + selecao, Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            });

            dialog.show();
        }

    }
}