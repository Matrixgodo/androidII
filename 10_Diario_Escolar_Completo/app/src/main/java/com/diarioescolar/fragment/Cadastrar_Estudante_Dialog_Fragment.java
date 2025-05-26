package com.diarioescolar.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.diarioescolar.R;
import com.google.android.material.textfield.TextInputEditText;

public class Cadastrar_Estudante_Dialog_Fragment extends DialogFragment {

    private TextInputEditText edtNome, edtIdade;
    private cadastrar_Estudante callback;

    public interface cadastrar_Estudante {
        void cadastrar_Estudante(String nome, Byte idade);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cadastro_aluno, null);

        TextInputEditText edtNome = view.findViewById(R.id.nome);
        TextInputEditText edtIdade = view.findViewById(R.id.idade);
        Button btnCadastrar = view.findViewById(R.id.btnCadastrar);
        Button btnCancelar = view.findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(v -> dismiss());// fecha janela

        btnCadastrar.setOnClickListener(v -> {
            String nome = edtNome.getText() != null ? edtNome.getText().toString() : "";
            String idadeStr = edtIdade.getText() != null ? edtIdade.getText().toString() : "";

            if (!nome.isEmpty() && !idadeStr.isEmpty()) {
                byte idade = Byte.parseByte(idadeStr);
                if (getActivity() instanceof cadastrar_Estudante) {// getActivity quem esta hospedando meu fragment dialog
                    ((cadastrar_Estudante) getActivity()).cadastrar_Estudante(nome, idade);
                }
                dismiss();
            }else {
                Toast.makeText(getContext(), "Digite os dados", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view); // definindo meu layout que inflaei no inicio
        return builder.create(); // criando meu alert dialog
    }

}
