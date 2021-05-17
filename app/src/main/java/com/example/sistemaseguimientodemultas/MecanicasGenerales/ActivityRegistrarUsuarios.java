package com.example.sistemaseguimientodemultas.MecanicasGenerales;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemaseguimientodemultas.Beans.Municipio;
import com.example.sistemaseguimientodemultas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityRegistrarUsuarios extends AppCompatActivity {

    private EditText edNombre;
    private EditText edApellido1;
    private EditText edApellido2;
    private EditText edEdad;
    private EditText edCurp;
    private EditText edCorreoElectronico;
    private EditText edContraseña1;
    private EditText edContraseña2;
    private EditText edFechaNacimiento;
    private Spinner municipio;
    private Button btnImagen;
    private Button btnRegistrarCuenta;

    private Bitmap bitmap;
    private Context context = this;
    private RequestQueue myqueue;
    private int PICK_IMAGE_REQUEST = 1;
    private List<Bitmap> listaImagenes;
    private List<Municipio> ListMunicipio;
    private String url2 = "http://sisemservice.online/municipios";
    private String contra1;
    private String contra2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuarios);
        edNombre = (EditText) findViewById(R.id.edNombre);
        edApellido1 = (EditText) findViewById(R.id.edApellido1);
        edApellido2 = (EditText) findViewById(R.id.edApellido2);
        edEdad= (EditText) findViewById(R.id.edEdad);
        edCurp = (EditText) findViewById(R.id.edCurp);
        edCorreoElectronico = (EditText) findViewById(R.id.edCorreoElectronico);
        edContraseña1 = (EditText) findViewById(R.id.edContraseña1);
        edContraseña2 = (EditText) findViewById(R.id.edContraseña2);
        edFechaNacimiento = (EditText) findViewById(R.id.edFechaNacimiento);

        municipio = (Spinner) findViewById(R.id.municipio);

        btnImagen = (Button) findViewById(R.id.btnImagen);
        btnRegistrarCuenta = (Button) findViewById(R.id.btnRegistar);

        listaImagenes = new ArrayList<Bitmap>();
        ListMunicipio = new ArrayList<Municipio>();
        contra1 = edContraseña1.getText().toString();
        contra2 = edContraseña2.getText().toString();

        myqueue = Volley.newRequestQueue(context);

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                btnImagen.setText("Inserta la Segunda Imagen");
            }
        });

        btnRegistrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrar();
            }
        });

        obtenerMunicipios();
    }

    public void Registrar() {
        if(contra1.equals(contra2)){
            String url = "http://sisemservice.online/usuario/nueva/persona";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, "Se ha registrado el usuario correctamente" , Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "EL usuario no ha sido registrado correctamente" , Toast.LENGTH_LONG).show();
                }
            }){
                protected Map<String, String> getParams() throws AuthFailureError{
                    int posM = municipio.getSelectedItemPosition();
                    String imgpre = getStringImagen(listaImagenes.get(0));
                    String imgpre2 = getStringImagen(listaImagenes.get(1));
                    Map<String, String> params = new HashMap<>();
                    params.put("nombre",edNombre.getText().toString());
                    params.put("primerApellido",edApellido1.getText().toString());
                    params.put("segundoApellido",edApellido2.getText().toString());
                    params.put("fechaNacimiento",edFechaNacimiento.getText().toString());
                    params.put("edad",edEdad.getText().toString());
                    params.put("curp",edCurp.getText().toString());
                    params.put("municipio",String.valueOf(ListMunicipio.get(posM).getId()));
                    params.put("imagen1",imgpre);
                    params.put("imagen2",imgpre2);
                    params.put("correo",edCorreoElectronico.getText().toString());
                    params.put("contrasenia",edContraseña1.getText().toString());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Error: Las contraseñas no coinciden");
            dialog.setIcon(R.drawable.advertensia);
            dialog.setMessage("Por favor, verifique que los datos sean los correctos");
            dialog.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edContraseña1.setText("");
                            edContraseña2.setText("");
                        }
                    });
            dialog.show();
        }
     }

    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona la Imagen"), PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                listaImagenes.add(bitmap);
                if (listaImagenes.size() == 2) {
                    listaImagenes.remove(1);
                    listaImagenes.add(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageByte = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodedImage;
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
}
