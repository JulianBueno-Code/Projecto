package com.example.mialdebu.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mialdebu.Models.Alumno;
import com.example.mialdebu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Alumno_Activity extends AppCompatActivity {





    FirebaseAuth mauth;
    FirebaseUser user;

    Alumno Data;
    EditText Nombre,Apellido,DNI;
    Spinner Año,Division,Turno,Especialidad;
    ProgressBar pb;
    String TNombre,TApellido;
    int Taño, TDiv, TTurno,TEsp;

    Button boton, botoncito;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_);
        Data = new Alumno();
        mauth = FirebaseAuth.getInstance();
        user = mauth.getCurrentUser();

        Año = findViewById(R.id.spinner_año);
        Division = findViewById(R.id.spinner_Div);
        Turno = findViewById(R.id.spinner_Turno);
        Especialidad = findViewById(R.id.spinner_Esp);


        Nombre = findViewById(R.id.edit_Nombre);
        Apellido = findViewById(R.id.edit_Apellido);
        DNI = findViewById(R.id.edit_DNI);
        botoncito = findViewById(R.id.Botoncito);
         boton = findViewById(R.id.btn_Alumno);
        int esp;

        pb = findViewById(R.id.progress_Alu);

       Año.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


               if(Año.getSelectedItemPosition() > 2){
                   Especialidad.setVisibility(View.VISIBLE);
               }else
                   Especialidad.setVisibility(View.INVISIBLE);


           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
       Especialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(position !=0)
                   ;
               else
                   if(Año.getSelectedItemPosition() >2)
                   Toast.makeText(Alumno_Activity.this, "Los Alumnos desde 4to año en adelante tienen una especialidad", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });



        botoncito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TDNI = DNI.getText().toString();
                   if(Comprobar()) {
                       Data.setNombre(Nombre.getText().toString());
                       Data.setApellido(Apellido.getText().toString());

                       Data.setDNI(TDNI);
                       if(Año.getSelectedItemPosition() >2)
                       Data.setEspecialidad(Especialidad.getSelectedItemPosition());
                       else
                           Data.setEspecialidad(0);
                       Data.setTurno(Turno.getSelectedItemPosition());
                       Data.setAño(Año.getSelectedItemPosition());
                       Data.setDivision(Division.getSelectedItemPosition());
                       Data.setUsuarioTutor(user.getUid());

                       AgregarAlumno(Data);
                   }else
                   {
                       Toast.makeText(Alumno_Activity.this, "Algun Campo falta por completar o Se encuentra mal escrito", Toast.LENGTH_SHORT).show();


                   }

                
            }
        });
        



    }

    private boolean Comprobar() {

        if(Nombre !=null && Apellido !=null && DNI != null)
        return true;
        else
            return false;
    }

    public void AgregarAlumno(Alumno data) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Alumnos").document(DNI.getText().toString()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    finish();
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        pb.setVisibility(View.INVISIBLE);


    }

    private  boolean TodoCorrecto()
    {



        return true;
    }



}
