package com.example.projetolifecycle;

import android.os.Handler;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Classe responsável por gerenciar um contador que atualiza um TextView
 * em intervalos fixos de tempo, respeitando o ciclo de vida do componente associado.
 */
public class Contador implements DefaultLifecycleObserver {

    // Tarefa agendada que controla a execução periódica do contador
    private ScheduledFuture<?> manipulador;

    // Executor responsável por agendar a tarefa de contagem
    private ScheduledExecutorService agendador;

    // TextView que exibirá o valor do contador
    private TextView tv;

    // Handler utilizado para postar atualizações na thread principal
    private Handler handler;

    // Valor atual do contador
    private int valor = 0;

    /**
     * Construtor da classe Contador.
     *
     * @param tv TextView que será atualizada com o valor do contador.
     * @param h Handler associado à thread principal para garantir atualização segura da UI.
     */
    public Contador(TextView tv, Handler h) {
        this.tv = tv;
        this.handler = h;
        this.agendador = Executors.newSingleThreadScheduledExecutor(); // Executor com uma única thread
    }

    /**
     * Método chamado automaticamente quando o ciclo de vida atinge o estado STARTED.
     * Inicia a contagem e atualiza o TextView a cada segundo.
     *
     * @param owner O LifecycleOwner que gerencia este ciclo de vida.
     */
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        manipulador = agendador.scheduleWithFixedDelay(() -> {
            valor++; // Incrementa o valor do contador
            handler.post(() -> {
                tv.setText(String.format("%d", valor)); // Atualiza o TextView na thread principal
            });
        }, 0, 1, TimeUnit.SECONDS); // Início imediato, repetição a cada 1 segundo
    }

    /**
     * Método chamado automaticamente quando o ciclo de vida atinge o estado STOPPED.
     * Cancela a tarefa de contagem para evitar desperdício de recursos.
     *
     * @param owner O LifecycleOwner que gerencia este ciclo de vida.
     */
    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        manipulador.cancel(true); // Cancela a tarefa agendada
    }
}
