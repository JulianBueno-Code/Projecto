package com.example.mialdebu.Models;


public class Alumno {

    private String nombre ;
    private String apellido;
    private String usuarioTutor,DNI;
    private int Año, Division,Turno,Especialidad;


    public String getUsuarioTutor() {
        return usuarioTutor;
    }

    public void setUsuarioTutor(String usuarioTutor) {
        this.usuarioTutor = usuarioTutor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getAño() {
        return Año;
    }

    public void setAño(int año) {
        Año = año;
    }

    public int getDivision() {


        return Division;
    }

    public void setDivision(int division) {
        Division = division;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public int getTurno() {
        return Turno;
    }

    public void setTurno(int turno) {
        Turno = turno;
    }

    public int getEspecialidad() {
        return Especialidad;
    }

    public void setEspecialidad(int especialidad) {
        Especialidad = especialidad;
    }
}
