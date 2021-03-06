package com.example.sistemaseguimientodemultas.SecretariaEstudioCuenta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sistemaseguimientodemultas.Fiscalia.ActivityHomeFiscalia;
import com.example.sistemaseguimientodemultas.MecanicasGenerales.LoginActivity;
import com.example.sistemaseguimientodemultas.R;
import com.example.sistemaseguimientodemultas.RecycleView.CustomAdapter;
import com.example.sistemaseguimientodemultas.RecycleView.ModelRecycler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ActivityVisorDemandSecretaria extends AppCompatActivity {

    private String estado;
    private String idDemanda;
    private String descripcionDemanda;
    private String tipoDemanda;
    private int num;

    private EditText edFecha;
    private EditText edHora;
    private EditText edMinutos;
    private TextView txtTipoDemanda;
    private TextView txtDescripcion;
    private TextView txtEstado;
    private Button btnAgendar;
    private TextView txtMensaje1;
    private TextView txtMensaje2;

    private String url;
    private Context context = this;
    private RequestQueue myqueue;
    private ProgressDialog progress;
    private List<ModelRecycler> listaPruebas1;
    private List<ModelRecycler> listaPruebas2;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;

    private String correoCiudadano;
    private String correoFiscal;

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
        setContentView(R.layout.activity_visor_demand_secretaria);

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

        listaPruebas1 = new ArrayList<>();
        listaPruebas2 = new ArrayList<>();
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerViewCiudadanoSecretaria);
        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerViewFiscaliaSecretaria);

        txtTipoDemanda = (TextView) findViewById(R.id.txtTipoDemanda);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        txtMensaje1 = (TextView) findViewById(R.id.txtMensaje3);
        txtMensaje2 = (TextView) findViewById(R.id.txtMensaje4);
        btnAgendar = (Button) findViewById(R.id.btnAgendar);

        edFecha = (EditText) findViewById(R.id.edFecha);
        edHora = (EditText) findViewById(R.id.edHora);
        edMinutos = (EditText) findViewById(R.id.edMinutos);

        idDemanda = getIntent().getStringExtra("idDemanda");
        descripcionDemanda = getIntent().getStringExtra("descripcionDemanda");
        tipoDemanda = getIntent().getStringExtra("tipoDemanda");

        txtDescripcion.setText(descripcionDemanda);
        txtTipoDemanda.setText(tipoDemanda);

        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                CrearAgenda();
                enviarCorreo();
                CambiarEstado();
            }
        });

        mostrarPruebas1();
        mostrarPruebas2();
    }

    public void CrearAgenda(){
        String hora = edHora.getText().toString();
        String minutos = edMinutos.getText().toString();
        String fecha = edFecha.getText().toString();

        String horaFinal = hora+":"+minutos;

        Map<String, Object> params = new HashMap<>();

        params.put("idDemanda",idDemanda);
        params.put("dia" , fecha);
        params.put("hora", horaFinal);

        JSONObject parameters = new JSONObject(params);

        String url1 = "http://sisemservice.online/audiencias/crear?json=";
        url1 = url1 + parameters.toString();

        System.out.println(url1);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.dismiss();
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

    public void mostrarPruebas1(){
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

                        listaPruebas1.add(new ModelRecycler(bitmap));

                        progress.dismiss();
                    }
                }catch(Exception e){
                    recyclerView1.setVisibility(View.INVISIBLE);
                    txtMensaje1.setVisibility(View.VISIBLE);
                    txtMensaje1.setText("No hay pruebas que mostrar para esta demanda.");

                    progress.dismiss();
                    e.printStackTrace();
                }

                CustomAdapter adapter = new CustomAdapter(listaPruebas1, context);
                recyclerView1.setHasFixedSize(true);
                recyclerView1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                recyclerView1.setAdapter(adapter);
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

    public void mostrarPruebas2(){
        Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("idDemanda", idDemanda);
        JSONObject parameters = new JSONObject(params);
        url = "http://sisemservice.online/pruebas/fiscal?json=";
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

                        String imagen = jsonObject.getString("pruebafiscalia");

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        byte[] imageBytes = baos.toByteArray();

                        imageBytes = Base64.decode(imagen, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                        listaPruebas2.add(new ModelRecycler(bitmap));

                        progress.dismiss();
                    }
                }catch(Exception e){
                    recyclerView2.setVisibility(View.INVISIBLE);
                    txtMensaje2.setVisibility(View.VISIBLE);
                    txtMensaje2.setText("No hay pruebas que mostrar para esta demanda.");

                    progress.dismiss();
                    e.printStackTrace();
                }

                CustomAdapter adapter = new CustomAdapter(listaPruebas2, context);
                recyclerView2.setHasFixedSize(true);
                recyclerView2.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                recyclerView2.setAdapter(adapter);
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

    public void CambiarEstado(){
        Map<String, Object> params = new HashMap<>();

        params.put("idDemanda", idDemanda);

        JSONObject parameters = new JSONObject(params);

        String url1 = "http://sisemservice.online/demanda/pleno?json=";
        url1 = url1 + parameters.toString();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Demanda agendada exitosamente");
                dialog.setIcon(R.drawable.advertensia);
                dialog.setMessage("La demanda ha sido agendada exitosamente.");
                dialog.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("Se a cambiado el estado");
                                Intent intent = new Intent(context, ActivitySecretariaEstudio.class);
                                intent.putExtra("nombre", nombre);
                                intent.putExtra("primerApellido", primerApellido);
                                intent.putExtra("segundoApellido", segundoApellido);
                                intent.putExtra("edad", edad);
                                intent.putExtra("curp", curp);
                                intent.putExtra("nombrerol", nombrerol);
                                intent.putExtra("correo", correo);
                                intent.putExtra("idpersona", idpersona);
                                intent.putExtra("idUsuario", idUsuario);
                                startActivity(intent);
                            }
                        });
                dialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        myqueue.add(jsonRequest);
    }

    public void enviarCorreo(){
        Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("idDemanda", idDemanda);
        JSONObject parameters = new JSONObject(params);
        url = "http://sisemservice.online/demandas/buscar/correos?json=";
        url = url + parameters.toString();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    correoCiudadano = response.get("correoUsr").toString();
                    correoFiscal = response.get("correoFsc").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Properties properties = new Properties();
                properties.put("mail.smtp.host","smtp.googlemail.com");
                properties.put("mail.smtp.socketFactory.port","465");
                properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                properties.put("mail.smtp.auth","true");
                properties.put("mail.smtp.port","465");

                try{
                    final String emailTJV = "TribunalFederaldelaJusticia@gmail.com";
                    final String password = "tribunalFederal";
                    Session session;
                    session= Session.getDefaultInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(emailTJV, password);
                        }
                    });

                    if(session != null){
                        javax.mail.Message message = new MimeMessage(session);

                        message.setFrom(new InternetAddress(emailTJV));
                        message.setSubject("Solicitaste cambiar tu contrase??a");
                        message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(correoFiscal.toString()));
                        message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(correoCiudadano.toString()));
                        message.setContent("La fecha agendada de tu demanda es: " + edFecha.getText().toString() + "\n" +
                                "esta pasara ha ser revisada y dada al pleno para el veredicto final." , "text/html; charset=utf-8");
                        Transport.send(message);
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }
}
