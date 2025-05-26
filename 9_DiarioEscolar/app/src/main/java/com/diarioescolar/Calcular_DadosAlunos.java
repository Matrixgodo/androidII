package com.diarioescolar;

import com.diarioescolar.modelo.EstudanteModel;

import java.util.ArrayList;
import java.util.List;

public class Calcular_DadosAlunos {


    public static double calcularNotaFinal(EstudanteModel estudante) {
        double soma = 0;
        for (Double nota : estudante.getNotas()) {
            soma += nota;
        }
        return soma / estudante.getNotas().size();
    }


    public static double calcularPercentualPresenca(EstudanteModel estudante) {
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

    public static String calcularSituacaoFinal(EstudanteModel estudante) {
        double notaFinal = calcularNotaFinal(estudante);
        double percentualPresenca = calcularPercentualPresenca(estudante);
        boolean aprovado = notaFinal >= 7.0 && percentualPresenca >= 75.0;
        return aprovado ? "Aprovado" : "Reprovado";
    }

    public static double calcularMediaGeralTurma(List<EstudanteModel> estudantes) {

        double somaMedias = 0;
        int totalEstudantes = 0;
        for (EstudanteModel estudante : estudantes) {
            double notaFinal = calcularNotaFinal(estudante);
            if (notaFinal >= 0) {
                somaMedias += notaFinal;
                totalEstudantes++;
            }
        }
        return totalEstudantes > 0 ? (somaMedias / totalEstudantes) : 0;
    }

    public static String getNomeAlunoMaiorNota(List<EstudanteModel> estudantes) {
        if (estudantes == null || estudantes.isEmpty()) {
            return "--";
        }
        double maiorNota = -1.0;
        String nomeMaiorNota = "--";
        for (EstudanteModel estudante : estudantes) {
            double notaFinal = calcularNotaFinal(estudante);
            if (notaFinal > maiorNota) {
                maiorNota = notaFinal;
                nomeMaiorNota = estudante.getNome();
            }
        }
        return nomeMaiorNota;
    }

    public static String getNomeAlunoMenorNota(List<EstudanteModel> estudantes) {
        if (estudantes == null || estudantes.isEmpty()) {
            return "--";
        }
        double menorNota = 11;
        String nomeMenorNota = "--";
        for (EstudanteModel estudante : estudantes) {
            double notaFinal = calcularNotaFinal(estudante);
            if (notaFinal > -1  && notaFinal < menorNota) {
                menorNota = notaFinal;
                nomeMenorNota = estudante.getNome();
            }
        }
        return nomeMenorNota;
    }

    public static double calcularMediaIdadeTurma(List<EstudanteModel> estudantes) {
        if (estudantes == null || estudantes.isEmpty()) {
            return 0;
        }
        int somaIdades = 0;
        int totalEstudantes = 0;
        for (EstudanteModel estudante : estudantes) {
            Byte idade = estudante.getIdade();
            if (idade != null) {
                somaIdades += idade;
                totalEstudantes++;
            }
        }
        return totalEstudantes > 0 ? (double) somaIdades / totalEstudantes : 0.0;
    }

    public static AlunosPorSituacao getAlunosPorSituacao(List<EstudanteModel> estudantes) {
        List<EstudanteModel> aprovados = new ArrayList<>();
        List<EstudanteModel> reprovados = new ArrayList<>();
        if (estudantes != null) {
            for (EstudanteModel estudante : estudantes) {
                if ("Aprovado".equals(calcularSituacaoFinal(estudante))) {
                    aprovados.add(estudante);
                } else {
                    reprovados.add(estudante);
                }
            }
        }
        return new AlunosPorSituacao(aprovados, reprovados);
    }

    /**
     * este sobre classe interna para encapsular as listas de alunos aprovad e reprovado.
     */
    public static class AlunosPorSituacao {
        private final List<EstudanteModel> aprovados;
        private final List<EstudanteModel> reprovados;

        public AlunosPorSituacao(List<EstudanteModel> aprovados, List<EstudanteModel> reprovados) {
            this.aprovados = aprovados;
            this.reprovados = reprovados;
        }

        public List<EstudanteModel> getAprovados() {
            return aprovados;
        }

        public List<EstudanteModel> getReprovados() {
            return reprovados;
        }
    }
}