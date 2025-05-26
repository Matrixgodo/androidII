package aulasbdd.estudante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EstatiticaGeral_Activity extends AppCompatActivity {

    TextView mediaGeral, nomeDoAlunoMaiorNota, nomeDoAlunoMenorNota, mediaIdadeDaTurma, listAprovados, listReprovados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatitica_geral);

        // Inicializando as views
        initViews();

        // Recuperando os dados passados pela Intent
        Intent it = getIntent();

        // Obtendo os dados passados pela Intent
        double mediaNotaGeral = it.getDoubleExtra("mediaNotaGeral", 0.0); // Valor padrão 0.0
        String situacao = it.getStringExtra("situacao");
        String nomeMaiorNota = it.getStringExtra("nomeMaiorNota");
        String nomeMenorNota = it.getStringExtra("nomeMenorNota");
        double mediaIdade = it.getDoubleExtra("mediaIdade", 0.0);
        String aprovados = it.getStringExtra("aprovados");
        String reprovados = it.getStringExtra("reprovados");

        // Exibindo os resultados nas TextViews
        mediaGeral.setText("Média Geral: " + mediaNotaGeral);
        nomeDoAlunoMaiorNota.setText("Maior Nota: " + nomeMaiorNota);
        nomeDoAlunoMenorNota.setText("Menor Nota: " + nomeMenorNota);
        mediaIdadeDaTurma.setText("Média Idade da Turma: " + mediaIdade);
        listAprovados.setText("Aprovados: " + aprovados);
        listReprovados.setText("Reprovados: " + reprovados);
    }

    private void initViews() {
        mediaGeral = findViewById(R.id.textMediaGeral);
        nomeDoAlunoMenorNota = findViewById(R.id.textMenorNota);
        nomeDoAlunoMaiorNota = findViewById(R.id.textMaiorNota);
        mediaIdadeDaTurma = findViewById(R.id.textMediaIdade);
        listAprovados = findViewById(R.id.textAprovados);
        listReprovados = findViewById(R.id.textReprovados);
    }
}
