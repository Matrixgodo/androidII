package aulasbdd.estudante;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Estudante implements Serializable {

    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("idade")
    @Expose
    private int idade;
    @SerializedName("notas")
    @Expose
    private List<Double> notas;
    @SerializedName("presenca")
    @Expose
    private List<Boolean> presenca;

    /**
     * No args constructor for use in serialization
     *
     */
    public Estudante() {
    }

    public Estudante(String nome, int idade, List<Double> notas, List<Boolean> presenca) {
        super();
        this.nome = nome;
        this.idade = idade;
        this.notas = notas;
        this.presenca = presenca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public List<Double> getNotas() {
        return notas;
    }

    public void setNotas(List<Double> notas) {
        this.notas = notas;
    }

    public List<Boolean> getPresenca() {
        return presenca;
    }

    public void setPresenca(List<Boolean> presenca) {
        this.presenca = presenca;
    }

}