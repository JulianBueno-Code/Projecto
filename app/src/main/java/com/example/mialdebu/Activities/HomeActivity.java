package com.example.mialdebu.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mialdebu.Adapters.Adapter_Comunicados;
import com.example.mialdebu.Fragments.HomeFragment;
import com.example.mialdebu.Fragments.ProfileFragment;
import com.example.mialdebu.Fragments.SettingsFragment;
import com.example.mialdebu.Models.Comunicado;
import com.example.mialdebu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.List;

public class HomeActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button comBtn,uBtn,alBtn,salirBtn;

    FloatingActionButton fab,nMultiuso ;
    final Fragment homeFragment = new HomeFragment();
    final Fragment settingsFragment = new SettingsFragment();
    final Fragment profileFragment = new ProfileFragment();
    private int ActualFragment;


    private  boolean adminAPP = false;

    List<Comunicado> mData;

    private RecyclerView RvCOmunHome;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAuth mAuth;

    private FirebaseUser currentuser;

    FirebaseFirestore db;
    Query RefComunicado;

    Adapter_Comunicados adapter_Comunicados;


    private String TAG = "Document Cheack";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        comBtn = findViewById(R.id.comunicadosBTN);
        alBtn = findViewById(R.id.uBTN);
        uBtn = findViewById(R.id.configBTN);
        salirBtn = findViewById(R.id.salirBTN);

        fab = findViewById(R.id.fabHome);
        nMultiuso = findViewById(R.id.fabMultiUso);

        comBtn.setOnClickListener(this);
        alBtn.setOnClickListener(this);
        salirBtn.setOnClickListener(this);
        uBtn.setOnClickListener(this);

        RvCOmunHome = findViewById(R.id.RvComun);

        layoutManager = new LinearLayoutManager (getApplicationContext ());

        RvCOmunHome.setLayoutManager(layoutManager);


             mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
       /* */

        db = FirebaseFirestore.getInstance();
        RefComunicado = db.collection("Comunicados");

        RefComunicado.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    mData = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Comunicado comunicado = document.toObject (Comunicado.class);


                            String s = new String ();
                            if (currentuser != null)
                                s = currentuser.getDisplayName ();
                            if (!comunicado.getSubidoPor ().contains (s)) {
                                db.collection ("Comunicados").document (document.getId ()).update ("VistoPor :", comunicado.getSubidoPor () + currentuser.getDisplayName () + ", ").addOnFailureListener (new OnFailureListener () {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d (TAG, e + "");
                                    }
                                });
                            }
                            mData.add (comunicado);
                        }
                        adapter_Comunicados = new Adapter_Comunicados (getApplicationContext (),mData);
                        RvCOmunHome.setAdapter (adapter_Comunicados);
                }

            }
        });

        nMultiuso.hide();
        getSupportActionBar().setTitle("Pagina Principal");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ActualFragment){
                    case 0:
                        getSupportFragmentManager().beginTransaction().remove(homeFragment).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().remove(profileFragment).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().remove(settingsFragment).commit();
                        break;


                }

                showButtons();
                getSupportActionBar().setTitle("Pagina Principal");

            }
        });


        nMultiuso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!adminAPP)
                switch (ActualFragment){
                    case 0:
                        Toast.makeText(HomeActivity.this, "Usted se encuentra en Los Comunicados", Toast.LENGTH_SHORT).show();

                        break;
                    case 1:
                        Toast.makeText(HomeActivity.this, "Usted se encuentra en Sus Alumnos", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(HomeActivity.this, "Usted esta en la configuracion", Toast.LENGTH_SHORT).show();
                        break;

                }
                else
                    switch (ActualFragment) {
                        case 0:
                            try {
                                Toast.makeText (HomeActivity.this, "Usted va a Subir un Comunicado", Toast.LENGTH_LONG).show ();
                                Intent i = new Intent (getApplicationContext (), ComunicadosActivity.class);
                                startActivity (i);
                            } catch (Exception e) {
                                Toast.makeText (HomeActivity.this, "Error : " + e, Toast.LENGTH_LONG).show ();
                            }


                            break;
                        case 1:
                            Toast.makeText (HomeActivity.this, "Usted Va a Administrar los alumnos", Toast.LENGTH_SHORT).show ();
                            try {
                                Intent i = new Intent (getApplicationContext (), AsistenciaActivity.class);
                                startActivity (i);
                            } catch (Exception e) {
                                Toast.makeText (HomeActivity.this, "Error : " + e, Toast.LENGTH_LONG).show ();
                            }
                            break;
                        case 2:
                            Toast.makeText (HomeActivity.this, "Usted esta en la configuracion", Toast.LENGTH_SHORT).show ();
                            break;
                    }
            }
        });

        RvCOmunHome.setVisibility(View.INVISIBLE);
    }

    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.comunicadosBTN:
                ActualFragment = 0;
                getSupportActionBar().setTitle("Comunicados");
                RvCOmunHome.setVisibility(View.VISIBLE);
                HideButtons();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragCOntain,homeFragment).commit();
                break;

            case R.id.uBTN:
                ActualFragment = 1;
                getSupportActionBar().setTitle("Alumnos");

                HideButtons();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragCOntain,profileFragment).commit();

                break;

            case R.id.configBTN:
                ActualFragment = 2;
                getSupportActionBar().setTitle("Configuracion");
                HideButtons();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragCOntain, settingsFragment).commit();

                break;

            case R.id.salirBTN:
                ActualFragment = 0;
                FirebaseAuth.getInstance().signOut();


                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                finish();
                break;

        }
    }

    private void HideButtons() {
        fab.show();
        nMultiuso.show();
                comBtn.setVisibility(View.INVISIBLE);
                uBtn.setVisibility(View.INVISIBLE);
                alBtn.setVisibility(View.INVISIBLE);
                salirBtn.setVisibility(View.INVISIBLE);

    }
    private void showButtons(){
        nMultiuso.hide();
        fab.hide();
        comBtn.setVisibility(View.VISIBLE);
        uBtn.setVisibility(View.VISIBLE );
        alBtn.setVisibility(View.VISIBLE);
        salirBtn.setVisibility(View.VISIBLE);


        RvCOmunHome.setVisibility(View.INVISIBLE);
    
    }
}
