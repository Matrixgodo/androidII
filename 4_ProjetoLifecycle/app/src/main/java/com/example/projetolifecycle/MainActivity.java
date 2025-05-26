package com.example.projetolifecycle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity é a atividade principal do aplicativo.
 * Ela é responsável por inicializar a interface do usuário,
 * configurar o contador e registrar o contador para observar
 * o ciclo de vida da Activity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define o layout da tela para esta Activity
        setContentView(R.layout.activity_main);

        // Cria um Handler associado à thread principal (UI thread)
        // Usado para atualizar o TextView de forma segura a partir de outras threads
        Handler handler = new Handler(Looper.myLooper());

        // Instancia o contador, passando o TextView que será atualizado e o Handler
        Contador contador = new Contador(findViewById(R.id.textview), handler);

        // Registra o contador como observador do ciclo de vida da Activity
        // Assim, o contador iniciará e parará automaticamente conforme os eventos de ciclo de vida
        getLifecycle().addObserver(contador);
    }
}
