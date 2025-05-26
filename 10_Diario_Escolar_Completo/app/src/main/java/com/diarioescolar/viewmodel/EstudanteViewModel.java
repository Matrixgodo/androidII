package com.diarioescolar.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.diarioescolar.repositorio.EstudanteRepositorio;
import com.diarioescolar.modelo.Estudante;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EstudanteViewModel extends ViewModel {
    // minha thread
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    // minha lista de estudante
    private  MutableLiveData<List<Estudante>> listMutableLiveData = new MutableLiveData<>();
    private  MutableLiveData<Estudante> estudanteByIdLiveData = new MutableLiveData<>();

    public LiveData<List<Estudante>> getListEstudanteLiveData() {
        return listMutableLiveData;
    }

    public LiveData<Estudante> getEstudanteByIdLiveData() {
        return estudanteByIdLiveData;
    }

    // busca lista de estudante mas sem dados completos dos estudantes
    public void buscarListaEstudantes() {
        executorService.execute(() -> {
            EstudanteRepositorio repoEstudante = new EstudanteRepositorio();
            List<Estudante> eLista = repoEstudante.buscarListaEstudantesIdNomeIdade();
            listMutableLiveData.postValue(eLista);
        });
    }

    // busca apenas estudante pelo id
    public void buscarEstudantePorId(int id){
        executorService.execute(()->{
            EstudanteRepositorio repoEstudante = new EstudanteRepositorio();
            Estudante e = repoEstudante.buscarEstudantePorId(id);
            estudanteByIdLiveData.postValue(e);
        });
    }

    public void cadastrarEstudante(Estudante e){
        executorService.execute(()->{
            EstudanteRepositorio repoEstudante = new EstudanteRepositorio();
            repoEstudante.cadastrarEstudante(e);
        });
    }

    private  void atualizarLista() {
        executorService.execute(() -> {
            EstudanteRepositorio repoEstudante = new EstudanteRepositorio();
            List<Estudante> lista = repoEstudante.buscarListaEstudantesIdNomeIdade();
            listMutableLiveData.postValue(lista);
        });
    }

    public void excluirEstudante(int id){
        executorService.execute(()->{
            EstudanteRepositorio estudanteRepositorio = new EstudanteRepositorio();
            estudanteRepositorio.excluirEstudantePorId(id);
            atualizarLista();
        });
    }

    public void atualizarEstudante(Estudante estudante) {
        executorService.execute(() -> {
            EstudanteRepositorio repoEstudante = new EstudanteRepositorio();
            repoEstudante.atualizarEstudante(estudante.getId(), estudante);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared(); // destroi minha view model
        //O shutdown() permitirá que tarefas enviadas anteriormente sejam executadas antes de encerrar
        executorService.shutdown();
    }

}