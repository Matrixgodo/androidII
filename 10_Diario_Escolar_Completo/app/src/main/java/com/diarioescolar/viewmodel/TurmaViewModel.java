package com.diarioescolar.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diarioescolar.modelo.Estudante;
import com.diarioescolar.modelo.Turma;
import com.diarioescolar.repositorio.EstudanteRepositorio;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TurmaViewModel extends ViewModel {

    private MutableLiveData<Turma> turmaLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Estudante>> listEstudantesMutableLiveData = new MutableLiveData<>();


    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public LiveData<List<Estudante>> getListEstudantesLiveData() {
        return listEstudantesMutableLiveData;
    }

    public LiveData<Turma> getTurmaLiveData() {
        return turmaLiveData;
    }

    public void carregarListaDeAlunos(List<Estudante> estudantesBase){
        executorService.execute(() -> {
            EstudanteRepositorio repositorio = new EstudanteRepositorio();
            List<Estudante> estudantesCompletos = repositorio.buscarListaEstudantesComDadosCompletos(estudantesBase);
            listEstudantesMutableLiveData.postValue(estudantesCompletos);
        });
    }


    public void carregarDadosTurma(List<Estudante> estudanteList) {
        executorService.execute(() -> {
            Turma turma = new Turma(estudanteList);
            turmaLiveData.postValue(turma);
        });
    }
}

