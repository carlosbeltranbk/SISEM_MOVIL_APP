package com.example.sistemaseguimientodemultas.Ciudadano;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemaseguimientodemultas.Beans.Municipio;
import com.example.sistemaseguimientodemultas.Beans.TipoDemanda;
import com.example.sistemaseguimientodemultas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityModificarDemanda extends AppCompatActivity {

    private Button btnModificar;
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
    private String descripcionDemanda;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_demanda);

        progress = new ProgressDialog(this);
        progress.setMessage("Modificando la demanda, por favor espere.");

        myqueue = Volley.newRequestQueue(this);

        btnModificar = (Button) findViewById(R.id.btnModificar);

        edDescripcion = (EditText) findViewById(R.id.edDescripcion);

        municipio = (Spinner) findViewById(R.id.municipio);
        tipoDemanda = (Spinner) findViewById(R.id.tipoDemanda);

        ListMunicipio = new ArrayList<Municipio>();
        ListTipoDemanda = new ArrayList<TipoDemanda>();

        idUser = getIntent().getStringExtra("idpersona");
        descripcionDemanda = getIntent().getStringExtra("descripcionDemanda");
        idDemanda = getIntent().getStringExtra("idDemanda");

        edDescripcion.setText(descripcionDemanda);

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

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                ModificarDemanda();
            }
        });
    }

    public void ModificarDemanda(){
        int posM = municipio.getSelectedItemPosition();
        int posD = tipoDemanda.getSelectedItemPosition();

        Map<String, Object> params = new HashMap<>();

        params.put("idDemanda", idDemanda);
        params.put("tipoDemanda", ListTipoDemanda.get(posD).getNombre());
        params.put("descripcion", edDescripcion.getText().toString());
        params.put("municipio", String.valueOf(ListMunicipio.get(posM).getId()));

        JSONObject parameters = new JSONObject(params);

        String url1 = "http://sisemservice.online/demanda/modificar?json=";
        url1 = url1 + parameters.toString();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.dismiss();
                Toast.makeText(context, "Se ha registrado correctamente", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, ActivityUploadImage.class);
                intent.putExtra("idDemanda", idDemanda);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        myqueue.add(jsonRequest);
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
}
