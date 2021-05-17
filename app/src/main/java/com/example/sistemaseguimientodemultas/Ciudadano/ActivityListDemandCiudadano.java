package com.example.sistemaseguimientodemultas.Ciudadano;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemaseguimientodemultas.Fiscalia.ActivityHomeFiscalia;
import com.example.sistemaseguimientodemultas.Fiscalia.ActivityPruebasFiscalia;
import com.example.sistemaseguimientodemultas.ListView.Adaptador;
import com.example.sistemaseguimientodemultas.OficialiaPartes.ActivityHomeOficialia;
import com.example.sistemaseguimientodemultas.OficialiaPartes.ActivityVisorDemandOficialia;
import com.example.sistemaseguimientodemultas.Pleno.ActivityHomePleno;
import com.example.sistemaseguimientodemultas.Pleno.ActivityVisorDemandPleno;
import com.example.sistemaseguimientodemultas.R;
import com.example.sistemaseguimientodemultas.SecretariaAcuerdos.ActivitySecretariaAcuerdos;
import com.example.sistemaseguimientodemultas.SecretariaEstudioCuenta.ActivitySecretariaEstudio;
import com.example.sistemaseguimientodemultas.SecretariaEstudioCuenta.ActivityVisorDemandSecretaria;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityListDemandCiudadano extends AppCompatActivity {

    private RequestQueue myqueue;
    private ListView listDemandas;
    private Context context = this;
    private TextView tv1;
    private ArrayList<HashMap<String, String>> listA;
    String url;
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
    ProgressDialog progress;

    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_demand_ciudadano);
        myqueue = Volley.newRequestQueue(this);

        progress = new ProgressDialog(this);
        progress.setMessage("Cargando registros, por favor espere.");

        listDemandas = (ListView) findViewById(R.id.listDemanda);

        btnVolver = (Button) findViewById(R.id.btnVolver);

        progress.show();

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nombrerol.equals("Ciudadano")) {
                    Intent intent = new Intent(context, ActivityHomeCiudadano.class);
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
                } else if (nombrerol.equals("Pleno")) {
                    Intent intent = new Intent(context, ActivityHomePleno.class);
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
                } else if (nombrerol.equals("Síndico Municipal")) {
                    Intent intent = new Intent(context, ActivityHomeFiscalia.class);
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
                } else if (nombrerol.equals("Secretaría de Estudio y Cuenta")) {
                    Intent intent = new Intent(context, ActivitySecretariaEstudio.class);
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
                } else if (nombrerol.equals("Oficialía de Partes")) {
                    Intent intent = new Intent(context, ActivityHomeOficialia.class);
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
            }
        });

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
        System.out.println("Rol este es el rol que esta en null: " + nombrerol);
        mostrarDemandas();
    }

    public void mostrarDemandas() {

        Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("idUsuario", idUsuario);
        JSONObject parameters = new JSONObject(params);

        if (nombrerol.equals("Ciudadano")) {
            url = "http:/sisemservice.online/demandas/usuario?json=";
            url = url + parameters.toString();
        } else {
            url = "http:/sisemservice.online/demandas";
        }

        System.out.println(url);

        JsonObjectRequest js = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray;

                System.out.println(response);
                try {
                    jsonArray = response.getJSONArray("demandas");

                    //Arreglo de Demandas
                    listA = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonDemanda = jsonObject.getJSONObject("demanda");

                        String idDemanda = jsonDemanda.getString("iddemanda");
                        String tipoDemanda = jsonDemanda.getString("tipodemanda");
                        String descripcionDemanda = jsonDemanda.getString("descripcion");
                        String estado = jsonDemanda.getString("estado");

                        HashMap<String, String> full_item_book = new HashMap<>();

                        full_item_book.put("idDemanda", idDemanda);
                        full_item_book.put("tipoDemanda", tipoDemanda);
                        full_item_book.put("descripcionDemanda", descripcionDemanda);
                        full_item_book.put("estado", estado);

                        if (nombrerol.equals("Ciudadano")) {
                            listA.add(full_item_book);
                        } else if (nombrerol.equals("Pleno")) {
                            if (estado.equals("7")) {
                                listA.add(full_item_book);
                            }
                        } else if (nombrerol.equals("Síndico Municipal")) {
                            if (estado.equals("1")) {
                                listA.add(full_item_book);
                            }else if(estado.equals("3")){
                                listA.add(full_item_book);
                            }
                        } else if (nombrerol.equals("Secretaría de Estudio y Cuenta")) {
                            if (estado.equals("6")) {
                                listA.add(full_item_book);
                            }
                        } else if (nombrerol.equals("Oficialía de Partes")) {
                            if (estado.equals("0")) {
                                listA.add(full_item_book);
                            }else if(estado.equals("2")){
                                listA.add(full_item_book);
                            }
                        }
                    }

                    listDemandas.setAdapter(new Adaptador(context, listA));


                    if (nombrerol.equals("Ciudadano")) {
                        listDemandas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(view.getContext(), ActivityDetalleDemanda.class);
                                String idDamanda = listA.get(position).get("idDemanda");
                                String estado = listA.get(position).get("estado");
                                String descripcionDemanda = listA.get(position).get("descripcionDemanda");
                                String tipoDemanda = listA.get(position).get("tipoDemanda");
                                intent.putExtra("idDemanda", idDamanda);
                                intent.putExtra("estado", estado);
                                intent.putExtra("descripcionDemanda", descripcionDemanda);
                                intent.putExtra("tipoDemanda", tipoDemanda);
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
                    } else if (nombrerol.equals("Pleno")) {
                        listDemandas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(view.getContext(), ActivityVisorDemandPleno.class);
                                String idDamanda = listA.get(position).get("idDemanda");
                                String estado = listA.get(position).get("estado");
                                String descripcionDemanda = listA.get(position).get("descripcionDemanda");
                                String tipoDemanda = listA.get(position).get("tipoDemanda");
                                intent.putExtra("idDemanda", idDamanda);
                                intent.putExtra("estado", estado);
                                intent.putExtra("descripcionDemanda", descripcionDemanda);
                                intent.putExtra("tipoDemanda", tipoDemanda);
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
                    } else if (nombrerol.equals("Síndico Municipal")) {
                        listDemandas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(view.getContext(), ActivityPruebasFiscalia.class);
                                String idDamanda = listA.get(position).get("idDemanda");
                                String estado = listA.get(position).get("estado");
                                String descripcionDemanda = listA.get(position).get("descripcionDemanda");
                                String tipoDemanda = listA.get(position).get("tipoDemanda");
                                intent.putExtra("idDemanda", idDamanda);
                                intent.putExtra("estado", estado);
                                intent.putExtra("descripcionDemanda", descripcionDemanda);
                                intent.putExtra("tipoDemanda", tipoDemanda);
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
                    } else if (nombrerol.equals("Secretaría de Estudio y Cuenta")) {
                        listDemandas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(view.getContext(), ActivityVisorDemandSecretaria.class);
                                String idDamanda = listA.get(position).get("idDemanda");
                                String estado = listA.get(position).get("estado");
                                String descripcionDemanda = listA.get(position).get("descripcionDemanda");
                                String tipoDemanda = listA.get(position).get("tipoDemanda");
                                intent.putExtra("idDemanda", idDamanda);
                                intent.putExtra("estado", estado);
                                intent.putExtra("descripcionDemanda", descripcionDemanda);
                                intent.putExtra("tipoDemanda", tipoDemanda);
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
                    } else if (nombrerol.equals("Oficialía de Partes")) {
                        listDemandas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(view.getContext(), ActivityVisorDemandOficialia.class);
                                String idDamanda = listA.get(position).get("idDemanda");
                                String estado = listA.get(position).get("estado");
                                String descripcionDemanda = listA.get(position).get("descripcionDemanda");
                                String tipoDemanda = listA.get(position).get("tipoDemanda");
                                intent.putExtra("idDemanda", idDamanda);
                                intent.putExtra("estado", estado);
                                intent.putExtra("descripcionDemanda", descripcionDemanda);
                                intent.putExtra("tipoDemanda", tipoDemanda);
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
                    }

                    progress.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        myqueue.add(js);
    }
}
