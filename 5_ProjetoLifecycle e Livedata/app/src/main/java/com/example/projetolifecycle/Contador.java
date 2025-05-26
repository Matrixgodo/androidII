package com.example.projetolifecycle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Contador implements DefaultLifecycleObserver {
    private ScheduledFuture<?> manipulador;
    private ScheduledExecutorService agendador;
    private MutableLiveData<Integer> valor;


    public Contador() {
        valor=new MutableLiveData<>();
        valor.setValue(0);
        agendador= Executors.newSingleThreadScheduledExecutor();
    }
    public LiveData<Integer> getValor(){
        return valor;
    }
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        manipulador= agendador.scheduleWithFixedDelay(()->{
            int v= valor.getValue();
            v++;
            valor.postValue(v); //porque está em outra thread
            //Log.i("teste",valor+"");
        },0,1, TimeUnit.SECONDS);
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        manipulador.cancel(true);
    }

}
