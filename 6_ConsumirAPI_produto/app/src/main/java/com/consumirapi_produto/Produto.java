package com.consumirapi_produto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Produto {

    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("valor")
    @Expose
    private Double valor;

    /**
     * No args constructor for use in serialization
     *
     */
    public Produto() {
    }

    public Produto(String nome, Double valor) {
        super();
        this.nome = nome;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}
