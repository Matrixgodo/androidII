package com.example.diarioescolar;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/estudantes")
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
    @GetMapping("{id}")
    ResponseEntity<Estudante> pegaEstudantePeloId(@PathVariable int id){
        Optional<Estudante> opcionalEstudante=estudantes.stream()
                .filter(estudante->estudante.getId()==id)
                .findFirst();
        return opcionalEstudante.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(new Estudante()));
    }
    private void inicializarDados() {
        estudantes.add(new Estudante(Serial.proximo(), "João", 20,
                List.of(7.5, 8.0, 6.5),
                List.of(true, false, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Maria", 22,
                List.of(9.0, 8.5, 7.0),
                List.of(true, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Pedro", 19,
                List.of(6.0, 7.0, 8.5),
                List.of(false, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Ana", 21,
                List.of(8.0, 9.0, 8.5),
                List.of(true, true, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Lucas", 23,
                List.of(7.0, 6.5, 7.5),
                List.of(false, false, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Carla", 24,
                List.of(8.5, 7.5, 9.0),
                List.of(true, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Rafael", 20,
                List.of(6.5, 7.0, 8.0),
                List.of(false, true, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Gabriel", 26,
                List.of(7.0, 8.5, 7.5),
                List.of(true, true, false, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Beatriz", 18,
                List.of(9.5, 9.0, 8.5),
                List.of(true, true, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Felipe", 23,
                List.of(6.5, 7.0, 6.0),
                List.of(false, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Juliana", 21,
                List.of(8.0, 7.5, 8.5),
                List.of(true, false, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Thiago", 22,
                List.of(7.0, 8.0, 6.5),
                List.of(true, true, false, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Sofia", 19,
                List.of(9.0, 8.5, 9.0),
                List.of(true, true, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Vinicius", 24,
                List.of(6.5, 7.0, 8.0),
                List.of(false, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Larissa", 20,
                List.of(8.5, 9.0, 7.5),
                List.of(true, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Mateus", 23,
                List.of(7.5, 6.5, 7.0),
                List.of(false, false, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Camila", 21,
                List.of(8.0, 8.5, 9.0),
                List.of(true, true, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Bruno", 25,
                List.of(6.0, 7.5, 6.5),
                List.of(false, true, false, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Letícia", 18,
                List.of(9.5, 9.0, 8.0),
                List.of(true, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Daniel", 22,
                List.of(7.0, 6.5, 7.5),
                List.of(true, false, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Isabela", 20,
                List.of(8.5, 8.0, 9.0),
                List.of(true, true, false, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Gustavo", 24,
                List.of(6.5, 7.0, 6.0),
                List.of(false, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Fernanda", 19,
                List.of(9.0, 8.5, 7.5),
                List.of(true, true, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Rodrigo", 23,
                List.of(7.5, 6.5, 8.0),
                List.of(false, false, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Natália", 21,
                List.of(8.0, 9.0, 8.5),
                List.of(true, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "André", 22,
                List.of(6.5, 7.0, 6.5),
                List.of(false, true, false, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Clara", 20,
                List.of(9.0, 8.5, 9.0),
                List.of(true, true, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Eduardo", 25,
                List.of(7.0, 6.5, 7.5),
                List.of(true, false, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Vitória", 19,
                List.of(8.5, 9.0, 8.0),
                List.of(true, true, false, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Henrique", 23,
                List.of(6.0, 7.5, 6.5),
                List.of(false, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Manuela", 21,
                List.of(9.0, 8.0, 8.5),
                List.of(true, true, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Leonardo", 22,
                List.of(7.5, 6.5, 7.0),
                List.of(false, false, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Aline", 20,
                List.of(8.5, 9.0, 8.0),
                List.of(true, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Marcos", 24,
                List.of(6.5, 7.0, 6.0),
                List.of(false, true, false, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Bianca", 19,
                List.of(9.0, 8.5, 9.0),
                List.of(true, true, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Igor", 23,
                List.of(7.0, 6.5, 7.5),
                List.of(true, false, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Lívia", 21,
                List.of(8.0, 9.0, 8.5),
                List.of(true, true, false, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Diego", 22,
                List.of(6.5, 7.0, 6.5),
                List.of(false, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Patrícia", 20,
                List.of(9.0, 8.5, 7.5),
                List.of(true, true, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Renato", 24,
                List.of(7.5, 6.5, 8.0),
                List.of(false, false, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Alice", 19,
                List.of(8.5, 9.0, 8.0),
                List.of(true, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Samuel", 23,
                List.of(6.0, 7.5, 6.5),
                List.of(false, true, false, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Laura", 21,
                List.of(9.0, 8.0, 8.5),
                List.of(true, true, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Caio", 22,
                List.of(7.0, 6.5, 7.0),
                List.of(true, false, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Helena", 20,
                List.of(8.5, 9.0, 8.0),
                List.of(true, true, false, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Fábio", 24,
                List.of(6.5, 7.0, 6.0),
                List.of(false, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Raquel", 19,
                List.of(9.0, 8.5, 9.0),
                List.of(true, true, true, true, false)));
        estudantes.add(new Estudante(Serial.proximo(), "Otávio", 23,
                List.of(7.5, 6.5, 7.5),
                List.of(false, false, true, true, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Valentina", 21,
                List.of(8.0, 9.0, 8.5),
                List.of(true, true, true, false, true)));
        estudantes.add(new Estudante(Serial.proximo(), "Júlio", 22,
                List.of(6.5, 7.0, 6.5),
                List.of(false, true, false, true, false)));
    }
}
