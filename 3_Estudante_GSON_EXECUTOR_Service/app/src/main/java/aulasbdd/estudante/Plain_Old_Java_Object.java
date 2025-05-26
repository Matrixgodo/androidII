package aulasbdd.estudante;

import java.util.List;

public class Plain_Old_Java_Object {
    //Resposta da API - Que no caso é um objeto que tem propriedade Estudante que é uma lista de objetos Estudante
    private List<Estudante> estudantes;

    public List<Estudante> getEstudantes() {
        return estudantes;
    }

    public void setEstudantes(List<Estudante> estudantes) {
        this.estudantes = estudantes;
    }
}
