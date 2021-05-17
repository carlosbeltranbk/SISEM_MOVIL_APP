package com.example.sistemaseguimientodemultas.Beans;

public class Municipio {
    private int id;
    private String municipio;

    public Municipio(int id, String municipio) {
        this.id = id;
        this.municipio = municipio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
