package com.diarioescolar.utils;

import com.diarioescolar.modelo.Estudante;

import java.util.ArrayList;
import java.util.List;

public class Calcular_DadosAlunos {


    public static double calcularNotaFinal(Estudante estudante) {
        double soma = 0;
        if (estudante != null && estudante.getNotas()!=null){
            for (Double nota : estudante.getNotas()) {
                soma += nota;
            }
            return soma / estudante.getNotas().size();
        }
        return 0;
    }


    public static double calcularPercentualPresenca(Estudante estudante) {
        List<Boolean> frequencia = estudante.getPresenca();
        if (frequencia == null || frequencia.isEmpty()) {
            return 0;
        }
        int totalAulas = frequencia.size();
        int presencas = 0;
        for (Boolean presente : frequencia) {
            if (presente) {
                presencas++;
            }
        }
        return (presencas * 100.0) / totalAulas;
    }

    public static String calcularSituacaoFinal(Estudante estudante) {
        double notaFinal = calcularNotaFinal(estudante);
        double percentualPresenca = calcularPercentualPresenca(estudante);
        boolean aprovado = notaFinal >= 7.0 && percentualPresenca >= 75.0;
        return aprovado ? "Aprovado" : "Reprovado";
    }







}