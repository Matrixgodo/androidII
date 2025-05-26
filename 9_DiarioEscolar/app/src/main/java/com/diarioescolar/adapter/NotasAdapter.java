package com.diarioescolar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.diarioescolar.R;

import java.util.List;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.ViewHolder> {
    private List<Double> notasList;

    public NotasAdapter(List<Double> notasList) {
        this.notasList = notasList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_simples, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double nota = notasList.get(position);
        holder.textView.setText(String.format("Nota: %.1f", nota));
    }

    @Override
    public int getItemCount() {
        return notasList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.nome);
        }
    }
}