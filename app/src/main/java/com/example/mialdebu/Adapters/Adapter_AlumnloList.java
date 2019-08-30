package com.example.mialdebu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mialdebu.Models.Alumno;
import com.example.mialdebu.R;

import java.util.List;

public class Adapter_AlumnloList extends RecyclerView.Adapter<Adapter_AlumnloList.ViewHolderAlumnoList> {

    Context mContext;

    List<Alumno> alumnos;



    public Adapter_AlumnloList(Context mContext, List<Alumno> MList)
    {
        this.mContext = mContext;
        this.alumnos = MList;

    }
    @NonNull
    @Override
    public Adapter_AlumnloList.ViewHolderAlumnoList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.alumno_listmodel,viewGroup,false);

        ViewHolderAlumnoList Holder = new ViewHolderAlumnoList (row);

        return Holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_AlumnloList.ViewHolderAlumnoList viewHolderAlumnoList, int i) {
        viewHolderAlumnoList.AsignarDatos(alumnos.get(i));

    }

    @Override
    public int getItemCount() {
        return alumnos.size ();
    }

    private String colocardatodesdeArray(int idDato, int Array) {
        String[] your_array = mContext.getResources().getStringArray(Array);
        String DatoConvertido = your_array[idDato];
        return DatoConvertido;
    }



    public class ViewHolderAlumnoList extends RecyclerView.ViewHolder {

        TextView Nombre =   itemView.findViewById(R.id.List_alumno_Nombre);
        TextView year =  itemView.findViewById(R.id.List_alumno_Año);
        TextView Division =itemView.findViewById(R.id.List_alumno_Division);
        TextView Dni = itemView.findViewById(R.id.List_alumno_Dni);
        TextView Turno = itemView.findViewById(R.id.List_alumno_Turno);
        TextView Especialidad = itemView.findViewById(R.id.List_alumno_Especialidad);

        public ViewHolderAlumnoList(@NonNull View itemView) {
            super (itemView);
        }

        public void AsignarDatos(Alumno alumno) {
            year.setText (colocardatodesdeArray (alumno.getAño (),R.array.Años));
            Division.setText (colocardatodesdeArray (alumno.getAño (),R.array.div));
            Especialidad.setText (colocardatodesdeArray (alumno.getAño (),R.array.Esp));
            Turno.setText (colocardatodesdeArray (alumno.getAño (),R.array.Turnos));

            Dni.setText (alumno.getDNI ());
            Nombre.setText (alumno.getNombre ());

        }
    }
}
