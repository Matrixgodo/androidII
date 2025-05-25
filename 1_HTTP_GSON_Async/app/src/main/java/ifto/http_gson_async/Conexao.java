package ifto.http_gson_async;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Conexao {
    public InputStream obterRespostaHTTP(String url){
        try {
            URL urlConexao = new URL(url);
            HttpURLConnection conexao = (HttpURLConnection) urlConexao.openConnection();
            conexao.setRequestMethod("GET");
            return  new BufferedInputStream(conexao.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String converter(InputStream inputStream){
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuilder stringBuilder = new StringBuilder();
        String linha;
        try {
            while ((linha = bufferedReader.readLine()) != null){
                stringBuilder.append(linha);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return stringBuilder.toString();
    }
}
