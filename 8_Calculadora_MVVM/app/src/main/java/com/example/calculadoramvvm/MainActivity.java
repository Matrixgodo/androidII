package com.example.calculadoramvvm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.calculadoramvvm.ViewModel.CalculadoraViewModel;


public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    private CalculadoraViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        Button b=findViewById(R.id.btCalcular);
        b.setOnClickListener(this);
        viewModel= new ViewModelProvider(this)
                .get(CalculadoraViewModel.class);

        viewModel.getResultado().observe(this,res -> {
                TextView tv=findViewById(R.id.textViewResultado);
                tv.setText("O resultado é: "+res);
            });
        viewModel.getErro().observe(this,s -> {
            if(!s.equals("")){
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                viewModel.limpaErro();
            }
        });
    }

    @Override
    public void onClick(View v) {
        EditText editTextNumero1=findViewById(R.id.editTextNumero1);
        EditText editTextNumero2=findViewById(R.id.editTextNumero2);
        RadioGroup radioGroup=findViewById(R.id.radiogroup);
        RadioButton radioButton=findViewById(radioGroup.getCheckedRadioButtonId());

        viewModel.calcular(editTextNumero1.getText().toString(),
                editTextNumero2.getText().toString(),radioButton.getText().toString());
    }

    /*@Override
    public void onDestroy(){
        super.onDestroy();
        if(isFinishing())
            Log.i("teste", "onDestroy: finalizou de vez");
        else
            Log.i("teste", "onDestroy: só viorou a tela");
    }*/
}
