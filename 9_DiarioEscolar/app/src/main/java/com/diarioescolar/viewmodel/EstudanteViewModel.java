package com.diarioescolar.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diarioescolar.Conexao;
import com.diarioescolar.modelo.EstudanteModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EstudanteViewModel extends ViewModel {
    private String BASE_URL = "https://192.168.1.14:8080/estudantes/";

    private  Conexao con;
    private  ExecutorService executorService;

    private  MutableLiveData<List<EstudanteModel>> estudanteLiveData = new MutableLiveData<>();
    private  MutableLiveData<EstudanteModel> estudanteByIdLiveData = new MutableLiveData<>();

    public EstudanteViewModel() {
        this.con = new Conexao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<EstudanteModel>> getEstudanteLiveData() {
        return estudanteLiveData;
    }

    public LiveData<EstudanteModel> getEstudanteByIdLiveData() {
        return estudanteByIdLiveData;
    }

    public void buscarListaEstudantes() {
        executorService.execute(() -> {
            InputStream ipStream = con.obterRespostaHTTP(BASE_URL);
            String textoJSON = con.converter(ipStream);

            Gson gson = new Gson();
            List<EstudanteModel> lista = gson.fromJson(textoJSON, new TypeToken<List<EstudanteModel>>(){}.getType());

            estudanteLiveData.postValue(lista);
        });
    }

    public void buscarEstudantePorId(int id) {
        executorService.execute(() -> {
            String url = BASE_URL +  id;
            InputStream ipStream = con.obterRespostaHTTP(url);
            String textoJSON = con.converter(ipStream);

            Gson gson = new Gson();
            EstudanteModel estudante = gson.fromJson(textoJSON, EstudanteModel.class);

            estudanteByIdLiveData.postValue(estudante);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared(); // destroi minha view model
        //executorService.shutdown();
    }
}
