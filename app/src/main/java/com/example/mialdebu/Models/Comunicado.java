package com.example.mialdebu.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Comunicado {

    private String Comunicado;
    private String Time;
    private String SubidoPor;
    private String VistoPor;

    public String getVistoPor() {
        return VistoPor;
    }

    public void setVistoPor(String vistoPor) {
        VistoPor = vistoPor;
    }

    public String getSubidoPor() {
        return SubidoPor;
    }

    public void setSubidoPor(String subidoPor) {
        SubidoPor = subidoPor;
    }

    public String getCurso() {
        return Curso;
    }

    public void setCurso(String curso) {
        Curso = curso;
    }

    private String Curso = new String();
    private String Divisiones = new String();

    public String getDivisiones() {
        return Divisiones;
    }


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setDivisiones(String ndivisiones) {

        this.Divisiones = ndivisiones ;

    }



    public String getComunicado() {
        return Comunicado;
    }

    public void setComunicado(String comunicado) {
        Comunicado = comunicado;
    }
}
