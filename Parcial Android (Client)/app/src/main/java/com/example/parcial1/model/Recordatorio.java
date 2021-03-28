package com.example.parcial1.model;

public class Recordatorio {

    private int x,y;
    private String texto,tipo, importancia;


    public Recordatorio(int x, int y, String importancia, String texto, String tipo) {
        this.x = x;
        this.y = y;
        this.importancia = importancia;
        this.texto = texto;
        this.tipo = tipo;
    }

    public Recordatorio() {
    }

    public String getTipo() {
        return tipo;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getImportancia() {
        return importancia;
    }

    public void setImportancia(String importancia) {
        this.importancia = importancia;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
