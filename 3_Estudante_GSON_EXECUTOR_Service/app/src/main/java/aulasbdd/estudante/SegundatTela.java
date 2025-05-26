package aulasbdd.estudante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SegundatTela extends AppCompatActivity {

    private TextView notaFinal, percentual, aprovacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_segundat_tela);
        Intent it = getIntent();
        initViews();

        notaFinal.setText(String.valueOf(String.format("%.2f",it.getDoubleExtra("nota",0))));
        percentual.setText(it.getIntExtra("porcentagem",0)+" %");
        aprovacao.setText(it.getStringExtra("aprovacao"));
    }

    private void initViews() {

        notaFinal = findViewById(R.id.textNotaFinal);
        percentual = findViewById(R.id.textPercentualPresenca);
        aprovacao = findViewById(R.id.textSituacaoFinal);
    }
}