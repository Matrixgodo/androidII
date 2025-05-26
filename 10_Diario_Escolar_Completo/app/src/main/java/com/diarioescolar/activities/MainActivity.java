package com.diarioescolar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diarioescolar.R;
import com.diarioescolar.adapter.NomeDoEstudante_Adapter;
import com.diarioescolar.fragment.Cadastrar_Estudante_Dialog_Fragment;
import com.diarioescolar.modelo.Estudante;
import com.diarioescolar.viewmodel.EstudanteViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NomeDoEstudante_Adapter.OnItemClickListener<Estudante>,
        NomeDoEstudante_Adapter.OnItemLongClickListener<Estudante>,
        View.OnClickListener,
        Cadastrar_Estudante_Dialog_Fragment.cadastrar_Estudante{

    private List<Estudante> estudanteList = new ArrayList<>();
    private NomeDoEstudante_Adapter nomeDoEstudanteAdapter;
    private RecyclerView recyclerView_DeNomes;
    private Button btn_TelaEstatisticaActivity, btn_DialogCadastrarFragment;
    private EstudanteViewModel estudanteViewModel;

    /**
     * Método chamado quando a Activity é criada.
     * Responsável por configurar a interface gráfica, inicializar componentes,
     * configurar o RecyclerView e seu Adapter, além de inicializar o ViewModel
     * e observar as mudanças nos dados dos estudantes para atualização da UI.
     *
     * @param savedInstanceState Bundle contendo o estado anterior da Activity, se houver.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define o layout da Activity
        setContentView(R.layout.activity_main);

        // Inicializa as referências dos componentes visuais da interface
        initViews();

        // Cria o Adapter para o RecyclerView, passando a lista de estudantes e os contextos necessários
        nomeDoEstudanteAdapter = new NomeDoEstudante_Adapter(estudanteList, this, this);
        // Associa o Adapter ao RecyclerView
        recyclerView_DeNomes.setAdapter(nomeDoEstudanteAdapter);

        // Configura os listeners de clique para os botões da interface
        btn_TelaEstatisticaActivity.setOnClickListener(this);
        btn_DialogCadastrarFragment.setOnClickListener(this);

        // Inicializa o ViewModel responsável pela lógica de dados dos estudantes
        estudanteViewModel = new ViewModelProvider(this).get(EstudanteViewModel.class);

        // Solicita ao ViewModel para buscar a lista de estudantes no repositório
        estudanteViewModel.buscarListaEstudantes();

        // Observa o LiveData contendo a lista de estudantes para atualizar a UI quando houver mudanças
        estudanteViewModel.getListEstudanteLiveData().observe(this, estudantes -> {
            if (estudantes != null) {
                // Atualiza a lista no Adapter e, consequentemente, a interface
                nomeDoEstudanteAdapter.atualizarLista(estudantes);
            } else {
                // Exibe mensagem de erro caso os dados não sejam carregados corretamente
                Toast.makeText(this, "Erro ao carregar estudantes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Inicializa as referências dos componentes visuais (Views) da Activity,
     * definindo também o gerenciador de layout do RecyclerView.
     */
    private void initViews() {
        // Localiza o RecyclerView no layout e configura o layout manager como LinearLayout (lista vertical)
        recyclerView_DeNomes = findViewById(R.id.recycler);
        recyclerView_DeNomes.setLayoutManager(new LinearLayoutManager(this));

        // Localiza os botões no layout para futuras interações
        btn_TelaEstatisticaActivity = findViewById(R.id.geral);
        btn_DialogCadastrarFragment = findViewById(R.id.cadastro);
    }


    /**
     * Manipula o evento de clique em um item da lista de estudantes.
     *
     * Ao clicar em um estudante, cria uma Intent para abrir a Activity de detalhes
     * do aluno, passando o ID do estudante selecionado como extra para que a outra
     * Activity possa carregar os dados corretos.
     *
     * @param item Estudante que foi clicado na lista.
     */
    @Override
    public void onItemClick(Estudante item) {
        Intent intencao = new Intent(this, DetalhesAlunoActivity.class);
        intencao.putExtra("id_estudante", item.getId());  // Passa o ID do estudante para a Activity destino
        startActivity(intencao);
    }

    /**
     * Manipula eventos de clique nos botões da interface principal da Activity.
     * Diferencia a ação a ser tomada conforme o ID da View clicada.
     *
     * @param v View que foi clicada.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.geral) {
            // Abre a Activity que exibe estatísticas gerais
            startActivity(new Intent(this, EstatisticaActivity.class));
        } else if (id == R.id.cadastro) {
            // Exibe o diálogo para cadastrar um novo estudante
            mostrarDialogCadastro();
        }
    }

    /**
     * Método responsável por exibir o diálogo de cadastro de estudante.
     *
     * Utiliza o FragmentManager da Activity para abrir o fragmento de diálogo
     * que contém o formulário para cadastro, permitindo que o usuário insira os dados.
     *
     * O método show() vincula o diálogo ao gerenciador de fragmentos compatível,
     * garantindo o controle correto do ciclo de vida do fragmento.
     */
    private void mostrarDialogCadastro() {
        new Cadastrar_Estudante_Dialog_Fragment()
                .show(getSupportFragmentManager(), "cadastro_dialog");
    }

    /**
     * Callback chamado quando um novo estudante é cadastrado via diálogo.
     * Recebe os dados fornecidos pelo usuário, cria uma nova instância de Estudante,
     * delega a operação de cadastro ao ViewModel e atualiza a lista observada.
     *
     * Exibe uma confirmação rápida ao usuário via Toast.
     *
     * @param nome  Nome do estudante cadastrado.
     * @param idade Idade do estudante cadastrada.
     */
    @Override
    public void cadastrar_Estudante(String nome, Byte idade) {
        Toast.makeText(this, "Funcionou", Toast.LENGTH_SHORT).show();

        // Cria um novo objeto Estudante com os dados recebidos
        Estudante e = new Estudante(nome, idade);

        // Solicita ao ViewModel o cadastro do novo estudante
        estudanteViewModel.cadastrarEstudante(e);

        // Atualiza a lista de estudantes para refletir a alteração na UI
        estudanteViewModel.buscarListaEstudantes();
    }

    /**
     * Método chamado quando o usuário realiza um clique longo sobre um item da lista de estudantes.
     * Exibe um diálogo de confirmação para exclusão do estudante selecionado.
     *
     * @param item O objeto {@link Estudante} que foi clicado longamente e está sujeito à exclusão.
     */
    @Override
    public void onItemLongClick(Estudante item) {
        // Criação da caixa de diálogo com estilo Material Design
        AlertDialog.Builder construtorCaixaDeDialogo = new MaterialAlertDialogBuilder(this);

        // Configurações do diálogo: título, mensagem e botões
        construtorCaixaDeDialogo
                .setTitle("Deseja excluir o estudante?")
                .setMessage("Ao clicar em 'Sim', o estudante será permanentemente removido.")

                // Ação para o botão positivo (SIM): executa a exclusão via ViewModel
                .setPositiveButton("SIM", (dialog, which) -> {
                    estudanteViewModel.excluirEstudante(item.getId());
                })

                // Ação para o botão negativo (NÃO): apenas fecha o diálogo
                .setNegativeButton("NÃO", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show(); // Exibe o diálogo
    }



}