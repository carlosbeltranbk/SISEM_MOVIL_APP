package com.example.sistemaseguimientodemultas.Ciudadano;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemaseguimientodemultas.Beans.Municipio;
import com.example.sistemaseguimientodemultas.Beans.TipoDemanda;
import com.example.sistemaseguimientodemultas.MecanicasGenerales.LoginActivity;
import com.example.sistemaseguimientodemultas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityNewDemanda extends AppCompatActivity {

    private Button btnRegistrar;
    private EditText edDescripcion;
    private Spinner municipio;
    private Spinner tipoDemanda;

    private Context context = this;
    private RequestQueue myqueue;
    private String url2 = "http://sisemservice.online/municipios";
    private List<Municipio> ListMunicipio;
    private List<TipoDemanda> ListTipoDemanda;
    private String idUser;
    private String idDemanda;
    String nombre;
    String primerApellido;
    String segundoApellido;
    String edad;
    String curp;
    String municipioUsuario;
    String nombrerol;
    String correo;
    String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_demanda);
        myqueue = Volley.newRequestQueue(this);

        btnRegistrar = (Button) findViewById(R.id.btnRegister);

        edDescripcion = (EditText) findViewById(R.id.edDescripcion);

        municipio = (Spinner) findViewById(R.id.municipio);
        tipoDemanda = (Spinner) findViewById(R.id.tipoDemanda);

        ListMunicipio = new ArrayList<Municipio>();
        ListTipoDemanda = new ArrayList<TipoDemanda>();

        idUser = getIntent().getStringExtra("idpersona");

        nombre = getIntent().getStringExtra("nombre");
        primerApellido = getIntent().getStringExtra("primerApellido");
        segundoApellido = getIntent().getStringExtra("segundoApellido");
        edad = getIntent().getStringExtra("edad");
        curp = getIntent().getStringExtra("curp");
        municipioUsuario = getIntent().getStringExtra("municipio");
        nombrerol = getIntent().getStringExtra("nombrerol");
        correo = getIntent().getStringExtra("correo");
        idUsuario = getIntent().getStringExtra("idUsuario");

        obtenerMunicipios();
        obtenerTipoDemanda();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarDemanda();
            }
        });

    }

    public void obtenerMunicipios() {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray;
                try {
                    jsonArray = response.getJSONArray("municipios");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject municipio = jsonArray.getJSONObject(i);
                        int id = municipio.getInt("idmunicipio");
                        String name = municipio.getString("nombremunicipio");

                        Municipio objMunicipio = new Municipio(id, name);
                        ListMunicipio.add(objMunicipio);
                    }
                    MunicipiosSpinner();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        myqueue.add(jsonRequest);
    }

    public void MunicipiosSpinner() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < ListMunicipio.size(); i++) {
            lables.add(ListMunicipio.get(i).getMunicipio());
        }

        ArrayAdapter<String> spinnerAdapterMunicipios = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                lables);

        spinnerAdapterMunicipios.setDropDownViewResource(
                R.layout.support_simple_spinner_dropdown_item);

        municipio.setAdapter(spinnerAdapterMunicipios);
    }


    public void obtenerTipoDemanda() {
        int id = 1;
        String name = "Transito";
        TipoDemanda objTipoDemanda = new TipoDemanda(id, name);

        int id2 = 2;
        String name2 = "Administrativa";
        TipoDemanda objTipoDemanda2 = new TipoDemanda(id2, name2);

        ListTipoDemanda.add(objTipoDemanda);
        ListTipoDemanda.add(objTipoDemanda2);
        TipoDemanda();
    }

    public void TipoDemanda() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < ListTipoDemanda.size(); i++) {
            lables.add(ListTipoDemanda.get(i).getNombre());
        }

        ArrayAdapter<String> spinnerAdapterTipoDemanda = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                lables);

        spinnerAdapterTipoDemanda.setDropDownViewResource(
                R.layout.support_simple_spinner_dropdown_item);
        tipoDemanda.setAdapter(spinnerAdapterTipoDemanda);
    }

    public void registrarDemanda() {
        int posM = municipio.getSelectedItemPosition();
        int posD = tipoDemanda.getSelectedItemPosition();

        Map<String, Object> params = new HashMap<>();

        params.put("idUsuario", idUser);
        params.put("tipoDemanda", ListTipoDemanda.get(posD).getNombre());
        params.put("descripcion", edDescripcion.getText().toString());
        params.put("municipio", String.valueOf(ListMunicipio.get(posM).getId()));

        JSONObject parameters = new JSONObject(params);

        String url1 = "http://sisemservice.online/demanda/crear?json=";
        url1 = url1 + parameters.toString();


        JsonObjectRequest js = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Intent intent = new Intent(context, ActivityUploadImage.class);
                    idDemanda = response.get("idDemanda").toString();
                    intent.putExtra("idDemanda", idDemanda);
                    intent.putExtra("nombre",nombre);
                    intent.putExtra("primerApellido" , primerApellido);
                    intent.putExtra("segundoApellido" , segundoApellido);
                    intent.putExtra("edad" , edad);
                    intent.putExtra("curp", curp);
                    intent.putExtra("municipio" , municipioUsuario);
                    intent.putExtra("nombrerol" , nombrerol);
                    intent.putExtra("correo" , correo);
                    intent.putExtra("idpersona" , idUser);
                    intent.putExtra("idUsuario", idUsuario);
                    startActivity(intent);

                } catch (JSONException e) {
                    Toast.makeText(context, "ERROR: error al registrar la demanda", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        js.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myqueue.add(js);
    }
}
