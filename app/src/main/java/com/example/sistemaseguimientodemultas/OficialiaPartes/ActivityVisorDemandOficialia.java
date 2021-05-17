package com.example.sistemaseguimientodemultas.OficialiaPartes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemaseguimientodemultas.Ciudadano.ActivityListDemandCiudadano;
import com.example.sistemaseguimientodemultas.R;
import com.example.sistemaseguimientodemultas.RecycleView.CustomAdapter;
import com.example.sistemaseguimientodemultas.RecycleView.ModelRecycler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityVisorDemandOficialia extends AppCompatActivity {

    private String estado;
    private String idDemanda;
    private String descripcionDemanda;
    private String tipoDemanda;
    private int num;

    private TextView txtTipoDemanda;
    private TextView txtDescripcion;
    private TextView txtEstado;
    private TextView txtMensaje;
    private Button btnRadicar;
    private Button btnSubsanar;
    private Button btnRechazar;
    private String url;
    private Context context = this;
    private RequestQueue myqueue;
    private ProgressDialog progress;
    private List<ModelRecycler> listaPruebas;
    private RecyclerView recyclerView;

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
        setContentView(R.layout.activity_visor_demand_oficialia);

        myqueue = Volley.newRequestQueue(this);

        progress = new ProgressDialog(this);
        progress.setMessage("Cargando registro, por favor espere.");

        progress.show();

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

        btnRadicar = (Button) findViewById(R.id.btnRadicar);
        btnSubsanar = (Button) findViewById(R.id.btnSubsanar);
        btnRechazar = (Button) findViewById(R.id.btnRechazar);

        txtTipoDemanda = (TextView) findViewById(R.id.txtTipoDemanda);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        txtEstado = (TextView) findViewById(R.id.txtEstado);

        idDemanda = getIntent().getStringExtra("idDemanda");
        estado = getIntent().getStringExtra("estado");
        descripcionDemanda = getIntent().getStringExtra("descripcionDemanda");
        tipoDemanda = getIntent().getStringExtra("tipoDemanda");
        txtMensaje = (TextView) findViewById(R.id.txtMensaje2);

        txtDescripcion.setText(descripcionDemanda);
        txtTipoDemanda.setText(tipoDemanda);

        listaPruebas = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCiudadanoOficialia);

        num = Integer.parseInt(estado);

        if(num == 0){
            txtEstado.setText("En espera");
        }else if(num == 1){
            txtEstado.setText("Aceptada");
        }else if(num == 2){
            txtEstado.setText("Subsanar");
            btnSubsanar.setVisibility(View.INVISIBLE);
        }else if(num == 3){
            txtEstado.setText("Rechazada");
        }

        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();

                params.put("idDemanda", idDemanda);
                JSONObject parameters = new JSONObject(params);

                String url1 = "http://sisemservice.online/demanda/eliminar?json=";
                url1 = url1 + parameters.toString();

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Se a modificado el estado de la demanda correctamente", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                myqueue.add(jsonRequest);
                Intent intent = new Intent(context, ActivityListDemandCiudadano.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("primerApellido", primerApellido);
                intent.putExtra("segundoApellido", segundoApellido);
                intent.putExtra("edad", edad);
                intent.putExtra("curp", curp);
                intent.putExtra("municipio", municipio);
                intent.putExtra("nombrerol", nombrerol);
                intent.putExtra("correo", correo);
                intent.putExtra("idpersona", idpersona);
                intent.putExtra("idUsuario", idUsuario);
                startActivity(intent);
            }
        });

        btnSubsanar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();

                params.put("idDemanda", idDemanda);
                JSONObject parameters = new JSONObject(params);

                String url1 = "http://sisemservice.online/demanda/prevenir?json=";
                url1 = url1 + parameters.toString();

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Se a modificado el estado de la demanda correctamente", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                myqueue.add(jsonRequest);

                Intent intent = new Intent(context, ActivityListDemandCiudadano.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("primerApellido", primerApellido);
                intent.putExtra("segundoApellido", segundoApellido);
                intent.putExtra("edad", edad);
                intent.putExtra("curp", curp);
                intent.putExtra("municipio", municipio);
                intent.putExtra("nombrerol", nombrerol);
                intent.putExtra("correo", correo);
                intent.putExtra("idpersona", idpersona);
                intent.putExtra("idUsuario", idUsuario);
                startActivity(intent);
            }
        });

        btnRadicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();

                params.put("idDemanda", idDemanda);
                JSONObject parameters = new JSONObject(params);

                String url1 = "http://sisemservice.online/demanda/radicar?json=";
                url1 = url1 + parameters.toString();

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Se a modificado el estado de la demanda correctamente", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                myqueue.add(jsonRequest);

                Intent intent = new Intent(context, ActivityListDemandCiudadano.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("primerApellido", primerApellido);
                intent.putExtra("segundoApellido", segundoApellido);
                intent.putExtra("edad", edad);
                intent.putExtra("curp", curp);
                intent.putExtra("municipio", municipio);
                intent.putExtra("nombrerol", nombrerol);
                intent.putExtra("correo", correo);
                intent.putExtra("idpersona", idpersona);
                intent.putExtra("idUsuario", idUsuario);
                startActivity(intent);
            }
        });

        mostrarPruebas();
    }

    public void mostrarPruebas(){
        Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("idDemanda", idDemanda);
        JSONObject parameters = new JSONObject(params);
        url = "http://sisemservice.online/pruebas/ciudadano?json=";
        url = url + parameters.toString();

        System.out.println(url);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray;
                System.out.println("response: " + response);
                try{
                    jsonArray = response.getJSONArray("pruebas");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String imagen = jsonObject.getString("pruebaciudadano");

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        byte[] imageBytes = baos.toByteArray();

                        imageBytes = Base64.decode(imagen, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                        listaPruebas.add(new ModelRecycler(bitmap));

                        progress.dismiss();
                    }
                }catch(Exception e){
                    recyclerView.setVisibility(View.INVISIBLE);
                    txtMensaje.setVisibility(View.VISIBLE);
                    txtMensaje.setText("No hay pruebas que mostrar para esta demanda.");

                    progress.dismiss();
                    e.printStackTrace();
                }

                CustomAdapter adapter = new CustomAdapter(listaPruebas, context);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                error.printStackTrace();
            }
        });

        myqueue.add(jsonRequest);
    }
}
