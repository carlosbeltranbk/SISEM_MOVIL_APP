package com.example.sistemaseguimientodemultas.ListView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sistemaseguimientodemultas.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Adaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private Context context;
    private ArrayList<HashMap<String, String>> datos;

    public Adaptador (Context context, ArrayList<HashMap<String, String>> datos){
        this.context = context;
        this.datos = datos;

        inflater = (LayoutInflater)
                context.getSystemService(
                        context.LAYOUT_INFLATER_SERVICE
                );
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View vista = inflater.inflate(R.layout.itemdemanda, null);

        TextView tipoDemanda = (TextView) vista.findViewById(R.id.txtTipoDemanda);
        TextView descripcionDemanda = (TextView) vista.findViewById(R.id.txtDescripcion);
        TextView idDemanda = (TextView) vista.findViewById(R.id.idDemanda);

        HashMap<String, String> item = datos.get(position);

        tipoDemanda.setText(item.get("tipoDemanda"));
        descripcionDemanda.setText(item.get("descripcionDemanda"));
        idDemanda.setText(item.get("idDemanda"));

        return vista;
    }
}
