package com.example.sistemaseguimientodemultas.RecycleView;

import android.graphics.Bitmap;

public class ModelRecycler {
    private Bitmap image;

    public ModelRecycler(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
