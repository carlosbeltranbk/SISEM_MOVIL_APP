package com.example.sistemaseguimientodemultas.Ciudadano;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemaseguimientodemultas.R;
import com.example.sistemaseguimientodemultas.RecycleView.CustomAdapter;
import com.example.sistemaseguimientodemultas.RecycleView.ModelRecycler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityUploadImage extends AppCompatActivity {

    private String idDemanda;
    private List<ModelRecycler> listaImagen;
    private List<Bitmap> lista;
    private RecyclerView recyclerView;
    private int i;
    private Button btnSubir;
    private Button btnRegistar;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String url = "http://sisemservice.online/pruebas/subir/jpg";
    private Context context = this;

    String nombre;
    String primerApellido;
    String segundoApellido;
    String edad;
    String curp;
    String municipioUsuario;
    String nombrerol;
    String correo;
    String idpersona;
    String idUsuario;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        progress = new ProgressDialog(this);
        progress.setMessage("Cargando imagen, por favor espere.");

        idDemanda = getIntent().getStringExtra("idDemanda");
        btnSubir = (Button) findViewById(R.id.btnSubir);
        btnRegistar = (Button) findViewById(R.id.btnRegistrar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewImage);
        listaImagen = new ArrayList<>();
        lista = new ArrayList<>();

        System.out.println("idDemanda -> " + idDemanda);

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityHomeCiudadano.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("primerApellido", primerApellido);
                intent.putExtra("segundoApellido", segundoApellido);
                intent.putExtra("edad", edad);
                intent.putExtra("curp", curp);
                intent.putExtra("municipio", municipioUsuario);
                intent.putExtra("nombrerol", nombrerol);
                intent.putExtra("correo", correo);
                intent.putExtra("idpersona", idpersona);
                intent.putExtra("idUsuario", idUsuario);
                startActivity(intent);
            }
        });

        nombre = getIntent().getStringExtra("nombre");
        primerApellido = getIntent().getStringExtra("primerApellido");
        segundoApellido = getIntent().getStringExtra("segundoApellido");
        edad = getIntent().getStringExtra("edad");
        curp = getIntent().getStringExtra("curp");
        municipioUsuario = getIntent().getStringExtra("municipio");
        nombrerol = getIntent().getStringExtra("nombrerol");
        correo = getIntent().getStringExtra("correo");
        idpersona = getIntent().getStringExtra("idpersona");
        idUsuario = getIntent().getStringExtra("idUsuario");
    }

    public void SubirImagen() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                Toast.makeText(ActivityUploadImage.this, "Se ha registro correctamente", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imgpre = getStringImagen(bitmap);
                Map<String, String> params = new HashMap<>();
                params.put("idDemanda", idDemanda);
                params.put("imagen", imgpre);
                System.out.println(params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona la Imagen"), PICK_IMAGE_REQUEST);
        progress.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("===============================================");
        System.out.println("RequestCode: " + requestCode);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                SubirImagen();
                listaImagen.add(new ModelRecycler(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        CustomAdapter adapter = new CustomAdapter(listaImagen, ActivityUploadImage.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityUploadImage.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageByte = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodedImage;
    }

}
