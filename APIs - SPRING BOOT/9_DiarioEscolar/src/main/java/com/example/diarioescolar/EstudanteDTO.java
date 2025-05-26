package com.example.diarioescolar;

import java.util.Objects;

public class EstudanteDTO {
    int id;
    String nome;
    int idade;
    public EstudanteDTO(){}
    public EstudanteDTO(Estudante estudante){
        this(estudante.getId(),estudante.getNome(),estudante.getIdade());
    }
    public EstudanteDTO(int id, String nome, int idade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EstudanteDTO that = (EstudanteDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EstudanteDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", idade=" + idade +
                '}';
    }
}
