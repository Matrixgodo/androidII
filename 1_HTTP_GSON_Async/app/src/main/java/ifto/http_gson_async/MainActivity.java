package ifto.http_gson_async;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Declaração de variáveis
    private TextView textViewDadosID;
    private final String URL = "https://jsonplaceholder.typicode.com/posts";
    private StringBuilder builder = null;
    private List<Item> listaDadosBaixados = null;
    private ObterDados obterDados = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewDadosID = findViewById(R.id.dadosID);
        obterDados = new ObterDados();
        obterDados.execute();
    }

    private class ObterDados extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Baixando dados...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Conexao conexao = new Conexao();
            InputStream inputStream = conexao.obterRespostaHTTP(URL);
            String respostaJSON = conexao.converter(inputStream);
            Log.i("respostaJSON", respostaJSON);
            Gson gson = new Gson();
            builder = new StringBuilder();

            if (respostaJSON != null) {
                try {
                    Type type = new TypeToken<List<Item>>() {}.getType();
                    listaDadosBaixados = gson.fromJson(respostaJSON, type);
                    for (Item item : listaDadosBaixados) {
                        builder.append("ID: ").append(item.getId())
                                .append(" - Título: ").append(item.getTitle())
                                .append(" \n\nDescricao: ").append(item.getBody())
                                .append("\n\n");
                    }
                    return true; // Sucesso
                } catch (Exception e) {
                    Log.e("GSON_ERROR", "Erro ao converter JSON", e);
                    return false;
                }
            } else {
                return false; // Falha ao obter JSON
            }
        }

        @Override
        protected void onPostExecute(Boolean sucesso) {
            super.onPostExecute(sucesso);
            if (sucesso && builder != null) {
                textViewDadosID.setText(builder.toString());
            } else {
                Toast.makeText(MainActivity.this, "Erro ao baixar dados ou converter JSON", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (obterDados != null && !obterDados.isCancelled()) {
            obterDados.cancel(true);
        }
    }
}
