package com.example.sistemaseguimientodemultas.MecanicasGenerales;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemaseguimientodemultas.Ciudadano.ActivityHomeCiudadano;
import com.example.sistemaseguimientodemultas.Fiscalia.ActivityHomeFiscalia;
import com.example.sistemaseguimientodemultas.Magistrados.ActivityHomeMagistrado;
import com.example.sistemaseguimientodemultas.OficialiaPartes.ActivityHomeOficialia;
import com.example.sistemaseguimientodemultas.Pleno.ActivityHomePleno;
import com.example.sistemaseguimientodemultas.R;
import com.example.sistemaseguimientodemultas.SecretariaAcuerdos.ActivitySecretariaAcuerdos;
import com.example.sistemaseguimientodemultas.SecretariaEstudioCuenta.ActivitySecretariaEstudio;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private Button btnLogin;
    private Button btnRegistrarCuenta;
    private Context context = this;

    //Valores utilizados por el sistema
    private String contraseña;
    private String usuario;


    //datos usuario
    String nombre;
    String primerApellido;
    String segundoApellido;
    String edad;
    String curp;
    String numeroTrabajador;
    String municipio;
    String nombrerol;
    String correo;
    String idpersona;
    String idUsuario;
    boolean estado;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);


        progress = new ProgressDialog(this);
        progress.setMessage("Cargando sesión, por favor espere.");

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarSesion();
            }
        });

        btnRegistrarCuenta = (Button) findViewById(R.id.btnRegistrarCuenta);

        btnRegistrarCuenta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityRegistrarUsuarios.class);
                startActivity(intent);
            }
        });
    }

    public void IniciarSesion() {
        progress.show();

        contraseña = mPasswordView.getText().toString();
        usuario = mEmailView.getText().toString();

        Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("correo", usuario);
        params.put("contrasenia", contraseña);

        JSONObject parameters = new JSONObject(params);

        String url = "http://sisemservice.online/login?json=";
        url = url + parameters.toString();

        System.out.println(url);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //objetos que retorna la consulta
                    JSONObject jsonUser = response.getJSONObject("usuario");
                    JSONObject jsonRol = jsonUser.getJSONObject("rol");
                    JSONObject jsonMunicipio = jsonUser.getJSONObject("municipio");

                    //obtencion de todos los parametros del usuario
                    nombre = jsonUser.getString("nombre");
                    idUsuario = jsonUser.getString("idUsuario");
                    primerApellido = jsonUser.getString("primerApellido");
                    segundoApellido = jsonUser.getString("segundoApellido");
                    edad = jsonUser.getString("edad");
                    curp = jsonUser.getString("curp");
                    numeroTrabajador = jsonUser.getString("numeroTrabajador");
                    municipio = jsonMunicipio.getString("nombremunicipio");
                    correo = jsonUser.getString("correo");
                    idpersona = response.getString("idPersona");
                    nombrerol = jsonRol.getString("nombrerol");
                    estado = jsonUser.getBoolean("estado");


                    if (response.get("status").toString().equals("success")) {
                        if(estado){
                            switch (nombrerol) {
                                case "Ciudadano":
                                    Intent intent1 = new Intent(LoginActivity.this, ActivityHomeCiudadano.class);
                                    intent1.putExtra("idpersona",idpersona);
                                    intent1.putExtra("idUsuario", idUsuario);
                                    intent1.putExtra("nombre", nombre);
                                    intent1.putExtra("correo",correo);
                                    intent1.putExtra("primerApellido" ,primerApellido);
                                    intent1.putExtra("segundoApellido" , segundoApellido);
                                    intent1.putExtra("edad" , edad);
                                    intent1.putExtra("curp",curp);
                                    intent1.putExtra("municipio" , municipio);
                                    intent1.putExtra("correo" , correo);
                                    intent1.putExtra("nombrerol" , nombrerol);
                                    startActivity(intent1);
                                    break;
                                case "Síndico Municipal":
                                    Intent intent3 = new Intent(LoginActivity.this, ActivityHomeFiscalia.class);
                                    intent3.putExtra("idpersona",idpersona);
                                    intent3.putExtra("nombre", nombre);
                                    intent3.putExtra("correo",correo);
                                    intent3.putExtra("primerApellido" ,primerApellido);
                                    intent3.putExtra("segundoApellido" , segundoApellido);
                                    intent3.putExtra("edad" , edad);
                                    intent3.putExtra("curp",curp);
                                    intent3.putExtra("numeroTrabajador" , numeroTrabajador);
                                    intent3.putExtra("municipio" , municipio);
                                    intent3.putExtra("correo" , correo);
                                    intent3.putExtra("nombrerol" , nombrerol);
                                    startActivity(intent3);
                                    break;
                                case "Secretaría de Estudio y Cuenta":
                                    Intent intent4 = new Intent(LoginActivity.this, ActivitySecretariaEstudio.class);
                                    intent4.putExtra("idpersona",idpersona);
                                    intent4.putExtra("nombre", nombre);
                                    intent4.putExtra("correo",correo);
                                    intent4.putExtra("primerApellido" ,primerApellido);
                                    intent4.putExtra("segundoApellido" , segundoApellido);
                                    intent4.putExtra("edad" , edad);
                                    intent4.putExtra("curp",curp);
                                    intent4.putExtra("numeroTrabajador" , numeroTrabajador);
                                    intent4.putExtra("municipio" , municipio);
                                    intent4.putExtra("correo" , correo);
                                    intent4.putExtra("nombrerol" , nombrerol);
                                    startActivity(intent4);
                                    break;
                                case "Pleno":
                                    Intent intent5 = new Intent(LoginActivity.this, ActivityHomePleno.class);
                                    intent5.putExtra("idpersona",idpersona);
                                    intent5.putExtra("nombre", nombre);
                                    intent5.putExtra("correo",correo);
                                    intent5.putExtra("primerApellido" ,primerApellido);
                                    intent5.putExtra("segundoApellido" , segundoApellido);
                                    intent5.putExtra("edad" , edad);
                                    intent5.putExtra("curp",curp);
                                    intent5.putExtra("numeroTrabajador" , numeroTrabajador);
                                    intent5.putExtra("municipio" , municipio);
                                    intent5.putExtra("correo" , correo);
                                    intent5.putExtra("nombrerol" , nombrerol);
                                    startActivity(intent5);
                                    break;
                                case "Oficialía de Partes":
                                    Intent intent7 = new Intent(LoginActivity.this, ActivityHomeOficialia.class);
                                    intent7.putExtra("idpersona",idpersona);
                                    intent7.putExtra("nombre", nombre);
                                    intent7.putExtra("correo",correo);
                                    intent7.putExtra("primerApellido" ,primerApellido);
                                    intent7.putExtra("segundoApellido" , segundoApellido);
                                    intent7.putExtra("edad" , edad);
                                    intent7.putExtra("curp",curp);
                                    intent7.putExtra("numeroTrabajador" , numeroTrabajador);
                                    intent7.putExtra("municipio" , municipio);
                                    intent7.putExtra("correo" , correo);
                                    intent7.putExtra("nombrerol" , nombrerol);
                                    startActivity(intent7);
                                    break;
                            }
                        }else{
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                            dialog.setTitle("Error: Usuario Inactivo");
                            dialog.setIcon(R.drawable.advertensia);
                            dialog.setMessage("El usuario no esta activo, por favor contacte a un administrador para que su cuenta sea activada de nuevo.");
                            dialog.setPositiveButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                        }
                                    });
                            dialog.show();
                        }
                    }
                } catch (Exception e) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                    dialog.setTitle("Error: Usuario Inexistente");
                    dialog.setIcon(R.drawable.advertensia);
                    dialog.setMessage("El usuario no esta registrado, por favor intentelo de nuevo, restablezca su contraseña o cree una nueva cuenta.");
                    dialog.setPositiveButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPasswordView.setText("");
                                }
                            });
                    dialog.show();
                    e.printStackTrace();
                }
                progress.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(this).add(jsonRequest);
    }

}

