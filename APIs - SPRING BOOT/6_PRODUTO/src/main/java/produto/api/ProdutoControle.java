package produto.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/produto")
public class ProdutoControle {
    private Produto p = new Produto("Televisão", 1000);

    @GetMapping
    public ResponseEntity<Produto> pegaProduto() {
        Random r = new Random();

        // Alterando o valor do produto com uma chance de aumento
        if (r.nextInt(10) < 1) {
            p.setValor(p.getValor() * 1.1);  // Aumenta 10% no valor do produto
        }

        // Alterando o nome do produto aleatoriamente
        if (r.nextInt(10) < 3) {
            p.setNome("Novo Produto " + r.nextInt(100));
        }

        return new ResponseEntity<>(p, HttpStatus.OK);
    }
}
