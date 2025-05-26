package aulasbdd.estudante;

import android.widget.EdgeEffect;

import java.util.ArrayList;
import java.util.List;

public class Calculo_Aluno {

    public static double notaFinal(List<Double> listaDeNotas){
        double notas=0;
        for (Double nota: listaDeNotas) {
             notas+= nota;
        }
        return notas/listaDeNotas.size();
    }

    public static int porcentagemPresenca(List<Boolean> presencaList){
        int presencaValida = 0;
        int numeroDeAulas = presencaList.size();
        for (Boolean presencaAluno: presencaList) {
            if (presencaAluno.booleanValue()){
                presencaValida += 1;
            }
        }
        return (int) (((double) presencaValida / numeroDeAulas) * 100);
    }

    public static String situacaoDeAprovacao(List<Boolean> presencaList,List<Double> listaDeNotas){
        double notaFinal = notaFinal(listaDeNotas);
        int percentual = porcentagemPresenca(presencaList);
        if (percentual>= 75 && notaFinal>= 7 && notaFinal<=10){
            return "Aprovado";
        }
        return "Reprovado";
    }

    public static double mediaNotaGeral(List<Estudante> listaEstudante) {
        double notasSomadas = 0;
        int quantidadeDeNotas = 0;

        for (Estudante a : listaEstudante) {
            for (Double nota : a.getNotas()) {
                notasSomadas += nota;
                quantidadeDeNotas++;
            }
        }

        if (quantidadeDeNotas == 0){
            return 0;
        }
        return notasSomadas / quantidadeDeNotas;

    }

    public static String maiorNota(List<Estudante> estudanteList){
        Estudante e=null;
        for (Estudante a : estudanteList){
            if (e!=null){
                if(notaFinal(e.getNotas())>notaFinal(a.getNotas())){
                    continue;
                }else {
                    e=a;
                }
            }else{
                e=a;
            }
        }
        return e.getNome();
    }

    public static String menorNota(List<Estudante> estudanteList){
        Estudante a = null;
        for (Estudante x: estudanteList) {
            double xNota = notaFinal(x.getNotas());
            if (a == null){
                a=x;
            }else{
                if (xNota < notaFinal(a.getNotas())){
                    a = x;
                }
            }
        }
        return a.getNome();
    }

    public static double mediaIdade(List<Estudante> estudanteList){
        double somaDeIdades = 0 ;
        int quantidade=0;
        for (Estudante a:estudanteList) {
            somaDeIdades += a.getIdade();
            quantidade++;
        }
        return quantidade != 0 ? somaDeIdades/quantidade : 0;
    }

    public static List<String> aprovados(List<Estudante> listEstudante){
        List<String> nomesAprovados = new ArrayList<>();
        for (Estudante a : listEstudante) {
            if ("Aprovado".equals(situacaoDeAprovacao(a.getPresenca(), a.getNotas()))){
                nomesAprovados.add(a.getNome());
            }
        }
        return  nomesAprovados;
    }

    public static List<String> reprovados(List<Estudante> listEstudante){
        List<String> nomesReprovados = new ArrayList<>();
        for (Estudante a : listEstudante) {
            if ("Reprovado".equals(situacaoDeAprovacao(a.getPresenca(), a.getNotas()))){
                nomesReprovados.add(a.getNome());
            }
        }
        return  nomesReprovados;
    }




}
