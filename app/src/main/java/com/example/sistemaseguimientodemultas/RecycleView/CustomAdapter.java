package com.example.sistemaseguimientodemultas.RecycleView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sistemaseguimientodemultas.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter{

    List<ModelRecycler> lista;
    public Context context;

    public CustomAdapter(List<ModelRecycler> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public CustomViewHolder(View view){
            super(view);
            image =  view.findViewById(R.id.imgPrueba);
        }

        public void binData (ModelRecycler view){
            image.setImageBitmap(view.getImage());
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((CustomViewHolder) viewHolder).binData(lista.get(i));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
