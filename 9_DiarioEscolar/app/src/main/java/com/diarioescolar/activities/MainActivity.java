package com.diarioescolar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diarioescolar.R;
import com.diarioescolar.adapter.NomesAlunos_Adapter;
import com.diarioescolar.modelo.EstudanteModel;
import com.diarioescolar.viewmodel.EstudanteViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NomesAlunos_Adapter.OnItemClickListener<EstudanteModel> , View.OnClickListener{
    private List<EstudanteModel> estudanteModelList = new ArrayList<>();
    private NomesAlunos_Adapter adapterDeNomes;
    RecyclerView rc;
    Button btn_telaEstatistica;
    private EstudanteViewModel estudanteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rc = findViewById(R.id.recycler);
        btn_telaEstatistica = findViewById(R.id.geral);
        rc.setLayoutManager(new LinearLayoutManager(this));
        adapterDeNomes = new NomesAlunos_Adapter(estudanteModelList, this);
        rc.setAdapter(adapterDeNomes);

        btn_telaEstatistica.setOnClickListener(this);

        estudanteViewModel = new ViewModelProvider(this).get(EstudanteViewModel.class);

        estudanteViewModel.getEstudanteLiveData().observe(this, estudantes -> {
            if (estudantes != null) {
                estudanteModelList.clear();
                estudanteModelList.addAll(estudantes);
                adapterDeNomes.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Erro ao carregar estudantes", Toast.LENGTH_SHORT).show();
            }
        });
        estudanteViewModel.buscarListaEstudantes();
    }

    @Override
    public void onItemClick(EstudanteModel item) {
        Intent intencao = new Intent(this, DetalhesAlunoActivity.class);
        intencao.putExtra("id_estudante", item.getId());
        startActivity(intencao);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.geral){
            Intent intencao = new Intent(this, EstatisticaActivity.class);
            startActivity(intencao);
        }
    }
}