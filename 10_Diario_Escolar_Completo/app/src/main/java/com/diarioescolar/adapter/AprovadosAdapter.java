package com.diarioescolar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diarioescolar.R;

import java.util.List;

public class AprovadosAdapter extends RecyclerView.Adapter<AprovadosAdapter.ViewHolder> {

    private List<String> nomesAprovados;

    public AprovadosAdapter(List<String> nomesAprovados) {
        this.nomesAprovados = nomesAprovados;
    }

    // INFLANDO ITEM NOTA FREQUENCIA
    @NonNull
    @Override
    public AprovadosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nota_frequencia, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AprovadosAdapter.ViewHolder holder, int position) {
        String nome = nomesAprovados.get(position);
        holder.nomeTextView.setText(nome);
    }

    @Override
    public int getItemCount() {
        return nomesAprovados != null ? nomesAprovados.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.nome);
        }
    }
}
