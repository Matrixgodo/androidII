package com.example.calculadoramvvm.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calculadoramvvm.Modelo.Calculadora;

public class CalculadoraViewModel extends ViewModel {
    private MutableLiveData<Double> resultado;
    private MutableLiveData<String> erro;
    public CalculadoraViewModel(){
        resultado=new MutableLiveData<>();
        erro=new MutableLiveData<>();
    }
    public LiveData<Double> getResultado(){
        return resultado;
    }
    public LiveData<String> getErro(){
        return erro;
    }
    public void limpaErro(){
        erro.setValue("");
    }
    public void calcular(String tn1,String tn2,String op){

        try {
            double n1 = Double.parseDouble(tn1);
            double n2 = Double.parseDouble(tn2);
            Calculadora c=new Calculadora(n1,n2,op);
            double res=c.calcular();
            resultado.setValue(res);
        }catch (NumberFormatException | NullPointerException e){
            erro.setValue("Informe os dados corretamente");
        }
    }

}
