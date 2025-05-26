package com.consumirapi_produto;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public class MainActivity extends AppCompatActivity implements Observer<Produto> {

    private TextView nome, valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        Contador contador = new Contador();

        // Avisando o contador sobre o ciclo de vida
        getLifecycle().addObserver(contador);
        contador.getProduto().observe(this, this);
    }

    @Override
    public void onChanged(Produto produto) {
        if (produto != null) {
            nome.setText(produto.getNome());
            valor.setText(String.valueOf(produto.getValor()));
            Log.i("teste", "Produto atualizado: " + produto.getNome() + " - " + produto.getValor());
        }
    }

    private void initViews() {
        nome = findViewById(R.id.nome);
        valor = findViewById(R.id.valor);
    }
}
