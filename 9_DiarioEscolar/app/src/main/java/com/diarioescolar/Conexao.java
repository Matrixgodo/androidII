package com.diarioescolar;

import android.net.SSLCertificateSocketFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class Conexao {

    public InputStream obterRespostaHTTP(String end) {
        try {
            URL url = new URL(end);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            // Verifica se é HTTPS
            if (conexao instanceof HttpsURLConnection) {
                HttpsURLConnection httpsCon = (HttpsURLConnection) conexao;

                // certificados autoassinados teste
                httpsCon.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                httpsCon.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                conexao = httpsCon;
            }

            conexao.setRequestMethod("GET");
            return conexao.getInputStream();

        } catch (IOException e) {
            throw new RuntimeException("Erro de conexão: " );
        }
    }

    public String converter(InputStream is) {
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();

        String conteudo;
        try {
            while ((conteudo = bufferedReader.readLine()) != null) {
                sb.append(conteudo).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o stream: " );
        }

        return sb.toString();
    }
}
