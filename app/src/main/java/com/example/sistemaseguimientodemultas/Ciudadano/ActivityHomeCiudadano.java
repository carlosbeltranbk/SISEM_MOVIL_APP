package com.example.sistemaseguimientodemultas.Ciudadano;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sistemaseguimientodemultas.MecanicasGenerales.LoginActivity;
import com.example.sistemaseguimientodemultas.R;

public class ActivityHomeCiudadano extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String nombre;
    String primerApellido;
    String segundoApellido;
    String edad;
    String curp;
    String municipio;
    String nombrerol;
    String correo;
    String idpersona;
    String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ciudadano);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Recoger los valores del Usuario
        nombre = getIntent().getStringExtra("nombre");
        primerApellido = getIntent().getStringExtra("primerApellido");
        segundoApellido = getIntent().getStringExtra("segundoApellido");
        edad = getIntent().getStringExtra("edad");
        curp = getIntent().getStringExtra("curp");
        municipio = getIntent().getStringExtra("municipio");
        nombrerol = getIntent().getStringExtra("nombrerol");
        correo = getIntent().getStringExtra("correo");
        idpersona = getIntent().getStringExtra("idpersona");
        idUsuario = getIntent().getStringExtra("idUsuario");

    }

    @Override
    public void onBackPressed() {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_demandas) {
            Intent intent = new Intent(this, ActivityListDemandCiudadano.class);
            intent.putExtra("nombre",nombre);
            intent.putExtra("primerApellido" , primerApellido);
            intent.putExtra("segundoApellido" , segundoApellido);
            intent.putExtra("edad" , edad);
            intent.putExtra("curp", curp);
            intent.putExtra("municipio" , municipio);
            intent.putExtra("nombrerol" , nombrerol);
            intent.putExtra("correo" , correo);
            intent.putExtra("idpersona" , idpersona);
            intent.putExtra("idUsuario", idUsuario);
            startActivity(intent);
        } else if (id == R.id.nav_crear) {
            Intent intent = new Intent(this, ActivityNewDemanda.class);
            intent.putExtra("nombre",nombre);
            intent.putExtra("primerApellido" , primerApellido);
            intent.putExtra("segundoApellido" , segundoApellido);
            intent.putExtra("edad" , edad);
            intent.putExtra("curp", curp);
            intent.putExtra("municipio" , municipio);
            intent.putExtra("nombrerol" , nombrerol);
            intent.putExtra("correo" , correo);
            intent.putExtra("idpersona" , idpersona);
            intent.putExtra("idUsuario", idUsuario);
            startActivity(intent);
        }else if(id == R.id.nav_cerrar){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
