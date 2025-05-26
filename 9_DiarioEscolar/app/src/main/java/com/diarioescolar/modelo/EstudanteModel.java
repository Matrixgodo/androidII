package com.diarioescolar.modelo;

import android.os.Parcelable;

import java.util.List;

/**
 * Classe modelo que representa um estudante no sistema.
 * <p>
 * <p>Os principais atributos são:
 * <ul>
 *     <li>{@code id} - Identificador único do estudante.</li>
 *     <li>{@code nome} - Nome completo do estudante.</li>
 *     <li>{@code idade} - Idade do estudante (utilizando {@code Byte} para economia de memória).</li>
 *     <li>{@code notas} - Lista de notas do estudante.</li>
 *     <li>{@code frequencia} - Lista que representa presença (true) ou falta (false) em aulas.</li>
 *     <li>{@code status} - Status acadêmico do estudante (ex: "Aprovado", "Reprovado").</li>
 * </ul>
 *
 *  * Classe modelo que representa um estudante no sistema.
 *  * Contém informações básicas como ID, nome, idade, lista de notas e frequência.
 *  * Implementa a interface Serializable para permitir que objetos sejam passados entre atividades.
 *  *
 *  * A idade foi declarada como Byte ao invés de int para reduzir o consumo de memória,
 *  * já que valores de idade raramente ultrapassam 127 (limite do tipo Byte).
 *

 */
public class EstudanteModel {
    private int id;
    private String nome;
    private Byte idade;
    private List<Double> notas;
    private List<Boolean> presenca;
    private String status = "Aprovado";

    /**
     * Construtor padrão necessário para frameworks e ferramentas que usam reflexão.
     */
    public EstudanteModel() {
    }

    /**
     * Construtor completo que inicializa todos os campos do estudante.
     *
     * @param id         Identificador do estudante.
     * @param nome       Nome do estudante.
     * @param idade      Idade do estudante.
     * @param notas      Lista de notas.
     * @param presenca Lista de presença/falta.
     * @param status     Status acadêmico.
     */
    public EstudanteModel(int id, String nome, Byte idade, List<Double> notas, List<Boolean> presenca, String status) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.notas = notas;
        this.presenca = presenca;
        this.status = status;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Byte getIdade() { return idade; }
    public void setIdade(Byte idade) { this.idade = idade; }
    public List<Double> getNotas() { return notas; }
    public void setNotas(List<Double> notas) { this.notas = notas; }
    public List<Boolean> getPresenca() { return presenca; }
    public void setPresenca(List<Boolean> presenca) { this.presenca = presenca; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
