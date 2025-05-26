package com.diarioescolar.modelo;

import static com.diarioescolar.utils.Calcular_DadosAlunos.calcularNotaFinal;
import static com.diarioescolar.utils.Calcular_DadosAlunos.calcularSituacaoFinal;

import java.util.ArrayList;
import java.util.List;

public class Turma {

    private double mediaGeralNotas;
    private double mediaIdade;
    private String nomeAlunoMaiorNota;
    private String nomeAlunoMenorNota;

    private List<String> nomesAprovados;
    private List<String> nomesReprovados;
    private List<Estudante> estudantes;

    // Construtor padrão
    public Turma() {
        this.estudantes = new ArrayList<>();
        this.nomesAprovados = new ArrayList<>();
        this.nomesReprovados = new ArrayList<>();
        this.nomeAlunoMaiorNota = "--";
        this.nomeAlunoMenorNota = "--";
        this.mediaGeralNotas = 0.0;
        this.mediaIdade = 0.0;
    }

    // Construtor com lista de estudantes
    public Turma(List<Estudante> estudantes) {
        this(); // chama o construtor padrão para inicializar os campos
        this.estudantes = estudantes;

        // Realiza os cálculos logo ao construir
        calcularMediaGeralNotas(estudantes);
        calcularMediaIdade(estudantes);
        definirAlunoMaiorNota(estudantes);
        definirAlunoMenorNota(estudantes);
        separarAprovadosReprovados(estudantes);
    }

    public void calcularMediaGeralNotas(List<Estudante> estudantes) {
        if (estudantes == null || estudantes.isEmpty()) return;

        double somaNotas = 0.0;
        int quantidade = 0;

        for (Estudante estudante : estudantes) {
            double nota = calcularNotaFinal(estudante);
            if (nota >= 0) {
                somaNotas += nota;
                quantidade++;
            }
        }

        this.mediaGeralNotas = (quantidade > 0) ? (somaNotas / quantidade) : 0.0;
    }

    public void calcularMediaIdade(List<Estudante> estudantes) {
        if (estudantes == null || estudantes.isEmpty()) return;

        int somaIdades = 0;
        int quantidade = 0;

        for (Estudante estudante : estudantes) {
            Byte idade = estudante.getIdade();
            if (idade != null) {
                somaIdades += idade;
                quantidade++;
            }
        }

        this.mediaIdade = (quantidade > 0) ? ((double) somaIdades / quantidade) : 0.0;
    }

    public void definirAlunoMaiorNota(List<Estudante> estudantes) {
        if (estudantes == null || estudantes.isEmpty()) return;

        double maiorNota = -1.0;
        String nome = "--";

        for (Estudante estudante : estudantes) {
            double nota = calcularNotaFinal(estudante);
            if (nota > maiorNota) {
                maiorNota = nota;
                nome = estudante.getNome();
            }
        }

        this.nomeAlunoMaiorNota = nome;
    }

    public void definirAlunoMenorNota(List<Estudante> estudantes) {
        if (estudantes == null || estudantes.isEmpty()) return;

        double menorNota = 11.0;
        String nome = "--";

        for (Estudante estudante : estudantes) {
            double nota = calcularNotaFinal(estudante);
            if (nota >= 0 && nota < menorNota) {
                menorNota = nota;
                nome = estudante.getNome();
            }
        }

        this.nomeAlunoMenorNota = nome;
    }

    public void separarAprovadosReprovados(List<Estudante> estudantes) {
        if (estudantes == null || estudantes.isEmpty()) return;

        this.nomesAprovados.clear();
        this.nomesReprovados.clear();

        for (Estudante estudante : estudantes) {
            String situacao = calcularSituacaoFinal(estudante);
            if ("Aprovado".equals(situacao)) {
                nomesAprovados.add(estudante.getNome());
            } else {
                nomesReprovados.add(estudante.getNome());
            }
        }
    }

    // Getters
    public double getMediaGeralNotas() {
        return mediaGeralNotas;
    }

    public double getMediaIdade() {
        return mediaIdade;
    }

    public String getNomeAlunoMaiorNota() {
        return nomeAlunoMaiorNota;
    }

    public String getNomeAlunoMenorNota() {
        return nomeAlunoMenorNota;
    }

    public List<String> getNomesAprovados() {
        return nomesAprovados;
    }

    public List<String> getNomesReprovados() {
        return nomesReprovados;
    }
}
