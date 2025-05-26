package com.example.diarioescolar;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("estudantes")
public class EstudantesControlador {
    List<Estudante> estudantes=new ArrayList<>();
    public EstudantesControlador(){
        inicializarDados();
    }
    @GetMapping("/")
    ResponseEntity<List<EstudanteDTO>> pegaEstudantes(){
        List<EstudanteDTO> estudantesDto=estudantes.stream().map(estudante -> new EstudanteDTO(estudante)).toList();
        return ResponseEntity.ok(estudantesDto);
    }
    @GetMapping("/{id}")
    ResponseEntity<Estudante> pegaEstudantePeloId(@PathVariable int id){
        Optional<Estudante> opcionalEstudante=estudantes.stream()
                .filter(estudante->estudante.getId()==id)
                .findFirst();
        return opcionalEstudante.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(new Estudante()));
    }

    @PostMapping("/")
    ResponseEntity<Estudante> criaEstudante(@RequestBody Estudante estudante) {
        estudante.setId(Serial.proximo());
        estudantes.add(estudante);
        System.out.println("Recebendo estudante: " + estudante);
        return ResponseEntity.ok(estudante);
    }

    @PutMapping("/{id}")
    ResponseEntity<Estudante> atualizaEstudante(@PathVariable int id, @RequestBody Estudante estudanteAtualizado) {
        Optional<Estudante> estudanteExistente = estudantes.stream()
                .filter(e -> e.getId() == id)
                .findFirst();

        if (estudanteExistente.isPresent()) {
            Estudante estudante = estudanteExistente.get();
            estudante.setNome(estudanteAtualizado.getNome());
            estudante.setIdade(estudanteAtualizado.getIdade());
            if (estudanteAtualizado.getNotas() != null) {
                estudante.getNotas().clear();
                estudante.getNotas().addAll(estudanteAtualizado.getNotas());
            }
            if (estudanteAtualizado.getPresenca() != null) {
                estudante.getPresenca().clear();
                estudante.getPresenca().addAll(estudanteAtualizado.getPresenca());
            }
            return ResponseEntity.ok(estudante);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> removeEstudante(@PathVariable int id) {
        if (estudantes.removeIf(e -> e.getId() == id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    private void inicializarDados() {
        Random random = new Random();
        String[] nomes = {"João", "Maria", "Pedro", "Ana", "Lucas", "Carla", "Rafael", "Gabriel", "Beatriz", "Felipe"};

        estudantes = IntStream.range(0, 3)// 1_000
                .parallel()
                .mapToObj(i -> {
                    String nome = nomes[random.nextInt(nomes.length)];
                    int idade = 18 + random.nextInt(13);
                    List<Double> notas = List.of(
                            0.5 * (random.nextInt(21)),
                            0.5 * (random.nextInt(22)),
                            0.5 * (random.nextInt(23))
                    );
                    List<Boolean> presencas = List.of(
                            random.nextBoolean(),
                            random.nextBoolean(),
                            random.nextBoolean(),
                            random.nextBoolean(),
                            random.nextBoolean()
                    );
                    return new Estudante(Serial.proximo(), nome, idade, notas, presencas);
                })
                .collect(Collectors.toCollection(() -> new ArrayList<>(3))); // 1_000
    }
}
