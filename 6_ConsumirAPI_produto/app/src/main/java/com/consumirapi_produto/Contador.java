package com.consumirapi_produto;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Contador implements DefaultLifecycleObserver {
    private ScheduledFuture<?> manipulador;
    private ScheduledExecutorService agendador;
    private ExecutorService executorService;
    private MutableLiveData<Produto> produto;
    private static final String URL = "http://192.168.1.14:8080/produto";

    public Contador() {
        produto = new MutableLiveData<>();
        agendador = Executors.newSingleThreadScheduledExecutor();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Produto> getProduto() {
        return produto;
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        manipulador = agendador.scheduleWithFixedDelay(() -> {
            executorService.execute(() -> {
                try {
                    Conexao conexao = new Conexao();
                    InputStream inputStream = conexao.obterRespostaHTTP(URL);
                    String json = conexao.converter(inputStream);

                    Gson gson = new Gson();
                    Produto novoProduto = gson.fromJson(json, Produto.class);

                    Produto produtoAtual = produto.getValue();
                    if (produtoAtual == null || !novoProduto.getValor().equals(produtoAtual.getValor())) {
                        produto.postValue(novoProduto); // Atualiza somente se mudar
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }, 0, 5, TimeUnit.SECONDS); // Cada 5 segundos
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        if (manipulador != null) {
            manipulador.cancel(true);
        }
    }
}
