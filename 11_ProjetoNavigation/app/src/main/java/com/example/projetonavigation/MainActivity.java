package com.example.projetonavigation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavHostFragment hostFragment= (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController=hostFragment.getNavController();

        DrawerLayout drawerLayout= findViewById(R.id.drawerlayout);
        NavigationView navigationView=findViewById(R.id.navigationview);
        appBarConfiguration=new AppBarConfiguration
                .Builder(navController.getGraph())
                .setOpenableLayout(drawerLayout)
                .build();
                //Builder(R.id.home,R.id.usuario).build();

        NavigationUI.setupWithNavController(navigationView,navController);
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return
                NavigationUI.navigateUp(navController,appBarConfiguration) ||
                super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navegacao,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return
                NavigationUI.onNavDestinationSelected(item,navController) ||
                super.onOptionsItemSelected(item);
    }

    public void irHome(View v){
        navController.navigate(R.id.home);
    }
    public void irUsuario(View v){
        navController.navigate(R.id.usuario);
    }
    public void irProduto(View v){
        navController.navigate(R.id.produto);
    }
    /*public void voltar(View v){
        *//*if(!navController.navigateUp())
            finish();*//*
        if(!navController.popBackStack(R.id.home,false))
            finish();
    }*/

   /* @Override
    public void onBackPressed() {
        if(!navController.navigateUp())
            super.onBackPressed();
    }*/
}