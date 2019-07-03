package com.example.mialdebu.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Comunicado {

    private String Comunicado;
    private String Time;



    private List<Boolean> divisiones = new ArrayList<>() ;
    private List<Boolean> Amos = new ArrayList<>() ;

    public List <Boolean> getDivisiones() {
        return divisiones;
    }


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setDivisiones(List<Boolean> ndivisiones) {

        this.divisiones = ndivisiones ;

    }

    public List<Boolean> getAmos() {

        return Amos;
    }

    public void setAmos(List<Boolean> naos) {
        this.Amos = naos;

    }

    public String getComunicado() {
        return Comunicado;
    }

    public void setComunicado(String comunicado) {
        Comunicado = comunicado;
    }
}
