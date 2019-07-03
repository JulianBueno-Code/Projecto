package com.example.mialdebu.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mialdebu.Models.Alumno;
import com.example.mialdebu.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Alumno extends RecyclerView.Adapter<Adapter_Alumno.ViewHolderAlumno> {

     static Context mContext;
    List<Alumno> ListAlumnos;


    public Adapter_Alumno(Context mContext, List<Alumno> MList)
    {
        this.mContext = mContext;
        this.ListAlumnos = MList;

    }


    @NonNull
    @Override
    public ViewHolderAlumno onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.raw_alumno,viewGroup,false);

        ViewHolderAlumno Holder = new ViewHolderAlumno(row, new ViewHolderAlumno.MyClickListener(){

            @Override
            public void OnEdit(int p) {
                String Dni = ListAlumnos.get(p).getDNI();

            }
        });

        return Holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAlumno viewHolderAlumno, int i) {


        Button btnAsistencia = viewHolderAlumno.itemView.findViewById(R.id.btn_Asistencia);
        Button btnNotas = viewHolderAlumno.itemView.findViewById(R.id.btn_Notas);
        viewHolderAlumno.AsignarDatos(ListAlumnos.get(i));


    }

    private static String colocarDivision(int i) {

        String[] your_array = mContext.getResources().getStringArray(R.array.div);
        String mdivision = your_array[i];
        return mdivision;
    }
    private static String colocarAño(int i) {
        String[] your_array = mContext.getResources().getStringArray(R.array.Años);
        String sear = your_array[i];
        return sear;
    }
    private static String colocarTurno(int i) {
        String[] your_array = mContext.getResources().getStringArray(R.array.Turnos);
        String Turno = your_array[i];
        return Turno;
    }
    private static String colocarEspecialidad(int i) {
        String[] your_array = mContext.getResources().getStringArray(R.array.Esp);
        String Especialidad = your_array[i];
        return Especialidad;
    }



    @Override
    public int getItemCount() {
        return ListAlumnos.size();
    }

    public static class ViewHolderAlumno extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Nombre =   itemView.findViewById(R.id.alumno_Nombre);
        TextView sear =  itemView.findViewById(R.id.alumno_Año);
        TextView Division =itemView.findViewById(R.id.alumno_Division);
        TextView Dni = itemView.findViewById(R.id.alumno_Dni);
        TextView Turno = itemView.findViewById(R.id.alumno_Turno);
        TextView Especialidad = itemView.findViewById(R.id.alumno_Especialidad);

           protected Button btnAsistencia = itemView.findViewById(R.id.btn_Asistencia);
        Button btnNotas ;

        MyClickListener listener;


        public ViewHolderAlumno(@NonNull View itemView, MyClickListener listener) {
            super(itemView);

            btnAsistencia = itemView.findViewById(R.id.btn_Asistencia);

            this.listener = listener;
            btnAsistencia.setOnClickListener(this);

        }

        public void AsignarDatos(Alumno alumno) {

           String TNombre = alumno.getNombre()+" ";
           String TApellido = alumno.getApellido();
           String Tsear = colocarAño(alumno.getAño());
           String TDivision = colocarDivision(alumno.getDivision());
           String TDNI = alumno.getDNI();
           String TTurno = colocarTurno(alumno.getTurno());
           String TEspecialidad = colocarEspecialidad(alumno.getEspecialidad());
            if(alumno.getEspecialidad() == 0)
                Especialidad.setVisibility(View.GONE);
            else
                Especialidad.setText(TEspecialidad);


            Nombre.setText(TNombre+TApellido);
            sear.setText(Tsear);
            Division.setText(TDivision);
            Turno.setText(TTurno);

            Dni.setText(String.valueOf(TDNI));
        }

        @Override
        public void onClick(View v) {
            listener.OnEdit(this.getLayoutPosition());
        }

        public interface MyClickListener {
            void OnEdit(int p);
        }
    }
}
