package com.example.calculadoramvvm.Modelo;

public class Calculadora {
    double n1,n2;
    String op;
    public Calculadora(double n1,double n2,String op){
        this.n1=n1;
        this.n2=n2;
        this.op=op;
    }
    public double calcular(){
        switch (op){
            case "Somar": return n1+n2;
            case "Subtrair": return n1-n2;
            case "Multiplicar": return n1*n2;
            case "Dividir": return n1/n2;
            default: throw new RuntimeException("Operação incorreta");
        }
    }
}
