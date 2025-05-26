package com.diarioescolar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.diarioescolar.R;
import java.util.List;

public class FrequenciaAdapter extends RecyclerView.Adapter<FrequenciaAdapter.ViewHolder> {
    private List<Boolean> frequenciaList;

    public FrequenciaAdapter(List<Boolean> frequenciaList) {
        this.frequenciaList = frequenciaList;
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
        boolean presenca = frequenciaList.get(position);
        holder.textView.setText(presenca ? "Presente" : "Faltou");
    }

    @Override
    public int getItemCount() {
        return frequenciaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.nome);
        }
    }
}