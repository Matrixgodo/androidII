package com.diarioescolar.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.diarioescolar.modelo.Estudante;
import com.diarioescolar.R;

import java.util.List;

/**
 * Adapter personalizado para o RecyclerView que exibe uma lista de estudantes.
 * É responsável por criar, vincular e gerenciar os itens da lista, utilizando um layout simples.
 * Essa classe conecta os dados da lista de objetos Estudante à interface gráfica, garantindo
 * um desempenho eficiente ao reutilizar as views com o padrão ViewHolder.
 */
public class NomeDoEstudante_Adapter extends RecyclerView.Adapter<NomeDoEstudante_Adapter.ViewHolder>  {
    private List<Estudante> estudanteList;

    private OnItemClickListener<Estudante> listener;
    private OnItemLongClickListener<Estudante> listenerLongo;

    public NomeDoEstudante_Adapter(List<Estudante> lista,
                                   OnItemClickListener<Estudante> listener,
                                   OnItemLongClickListener listenerLongo) {
        this.estudanteList = lista;
        this.listener = listener;
        this.listenerLongo = listenerLongo;
    }

    public void atualizarLista(List<Estudante> novaLista) {
        this.estudanteList.clear();
        this.estudanteList.addAll(novaLista);
        notifyDataSetChanged();
    }

    /**
     * Responsável por inflar o layout de item individual da lista (item_lista_simples.xml),
     * criando e retornando uma nova instância de ViewHolder.
     *
     * @param viewGroup O grupo pai que irá conter a nova visualização.
     * @param i Posição do item (não utilizada nesse caso).
     * @return ViewHolder com a view inflada.
     */
    @NonNull
    @Override
    public NomeDoEstudante_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_simples, viewGroup, false);
        return new ViewHolder(v);
    }

    /**
     * Vincula os dados do objeto Estudante ao item da lista (TextView) para a posição especificada.
     * Ideal para configurar cliques e interações com o item, utilizando os dados disponíveis.
     *
     * @param holder O ViewHolder que deve ser atualizado com os dados do item.
     * @param i A posição do item na lista.
     */
    @Override
    public void onBindViewHolder(@NonNull NomeDoEstudante_Adapter.ViewHolder holder, int i) {
        Estudante estudante = estudanteList.get(i);
        holder.tv_nome.setText(estudante.getNome());
    }

    /**
     * Retorna o número total de itens a serem exibidos no RecyclerView.
     * Isso informa ao adapter quantos elementos precisam ser gerenciados.
     *
     * @return Tamanho da lista de estudantes.
     */
    @Override
    public int getItemCount() {
        return estudanteList.size();
    }



    /**
     * Classe interna que representa o ViewHolder personalizado.
     * Armazena referências das views para evitar chamadas repetidas de findViewById,
     * otimizando o desempenho da lista.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nome;
        private Button btn_add_frequencia, btn_add_nota;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_nome = itemView.findViewById(R.id.nome);

            tv_nome.setOnClickListener(v -> {
                listener.onItemClick(estudanteList.get(getAdapterPosition()));
            });

            tv_nome.setOnLongClickListener(v -> {
                listenerLongo.onItemLongClick(estudanteList.get(getAdapterPosition()));
                return false;
            });
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public interface OnItemLongClickListener<T>{
        void onItemLongClick(T item);
    }
}
