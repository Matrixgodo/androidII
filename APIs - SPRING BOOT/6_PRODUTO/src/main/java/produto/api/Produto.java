package produto.api;

import java.io.Serializable;

public class Produto implements Serializable {
    private String nome;
    private double valor;


    public Produto(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    public Produto() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

}
