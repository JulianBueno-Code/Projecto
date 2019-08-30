package com.example.mialdebu.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mialdebu.Adapters.Adapter_AlumnloList;
import com.example.mialdebu.Models.Alumno;
import com.example.mialdebu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AsistenciaActivity extends AppCompatActivity {


    private FirebaseFirestore firebaseFirestore;

    private Spinner Spindiv,Spinyear;
    private Query Refdatabe;
    Button AsisB;
    FloatingActionButton FabBack;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView Rv;

    ProgressBar pb1,pb2;

    List<Alumno> alumnos;
    private Adapter_AlumnloList adapter;
    private ArrayList<Alumno> transitoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);


        Spindiv = findViewById(R.id.asis_spin_div);
        Spinyear = findViewById(R.id.asis_spin_year);

        FabBack = findViewById (R.id.fabAsis);
        AsisB = findViewById (R.id.asisBtnB);
        pb1 = findViewById (R.id.asisPB);
        pb2 = findViewById (R.id.asisPB2);

        layoutManager = new LinearLayoutManager (getApplicationContext ());
        Rv = findViewById (R.id.RvAsis);
        Rv.setLayoutManager (layoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Refdatabe = firebaseFirestore.collection("Alumnos");
        primer (Spindiv.getSelectedItemId (),Spinyear.getSelectedItemId ());




        showThings ();

        FabBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getApplicationContext (),HomeActivity.class);
                startActivity (i);
            }
        });
        AsisB.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                ConseguirAlumnos (Spindiv.getSelectedItemId (),Spinyear.getSelectedItemId ());
                hideThings ();
            }
        });

    }

    private  void showThings(){
        pb1.setVisibility (View.INVISIBLE);
        pb2.setVisibility (View.INVISIBLE);


        Rv.setVisibility (View.VISIBLE);
        AsisB.setVisibility (View.VISIBLE);
        Spindiv.setEnabled (true);
        Spinyear.setEnabled (true);

    }

    private  void hideThings(){
        pb1.setVisibility (View.VISIBLE);
        pb2.setVisibility (View.VISIBLE);

        Rv.setVisibility (View.INVISIBLE);
        AsisB.setVisibility (View.INVISIBLE);
        Spindiv.setEnabled (false);
        Spinyear.setEnabled (false);

    }




    private void ConseguirAlumnos(long Division, long Año) {

        Refdatabe.whereEqualTo ("año",Año).
        whereEqualTo ("division",Division).get ().addOnCompleteListener (new OnCompleteListener<QuerySnapshot> () {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful ()) {
                    transitoryList= new ArrayList<> ();


                    for (DocumentSnapshot document : task.getResult ()) {
                        Alumno al = document.toObject (Alumno.class);
                        if(al != null) {
                            Log.d ("tagcito", al.toString ());
                            transitoryList.add (al);
                        }
                    }
                    alumnos.clear ();
                    alumnos.addAll (transitoryList);
                    adapter.notifyDataSetChanged ();
                    showThings ();

                }else
                    showThings ();

            }
        }).addOnFailureListener (new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText (AsistenciaActivity.this, "Error : "+e, Toast.LENGTH_SHORT).show ();
                showThings ();
            }
        });



        showThings ();
    }



    private void primer(long Division, long Año) {

        Refdatabe.whereEqualTo ("año",Año).
                whereEqualTo ("division",Division).get ().addOnCompleteListener (new OnCompleteListener<QuerySnapshot> () {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful ()) {
                    alumnos = new ArrayList<> ();

                    for (DocumentSnapshot document : task.getResult ()) {
                        Alumno al = document.toObject (Alumno.class);


                        alumnos.add (al);

                    }
                    adapter = new Adapter_AlumnloList (getApplicationContext (), alumnos);
                    Rv.setAdapter (adapter);

                }else
                    showThings ();

            }
        }).addOnFailureListener (new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText (AsistenciaActivity.this, "Error : "+e, Toast.LENGTH_SHORT).show ();
                showThings ();
            }
        });



        showThings ();
    }







}
